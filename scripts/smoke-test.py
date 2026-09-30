#!/usr/bin/env python3
"""Read-only checks for the running Compose stack; uses only Python's standard library."""
import json
import os
import re
from urllib.request import urlopen


ui = os.environ.get("UI_URL", "http://localhost:" + os.environ.get("UI_PORT", "3000"))
backend = os.environ.get("BACKEND_URL", "http://localhost:" + os.environ.get("BACKEND_PORT", "8080"))


def get(url):
    with urlopen(url, timeout=15) as response:
        assert response.status == 200, (url, response.status)
        return response.headers.get("Content-Type", ""), response.read()


content_type, html = get(ui + "/")
assert "text/html" in content_type
assert b'<div id="root"></div>' in html, "React root is missing"
scripts = re.findall(rb'<script[^>]+src="([^"]+)"', html)
assert scripts, "No JavaScript bundle found"
for script in scripts:
    content_type, bundle = get(ui + script.decode())
    assert "javascript" in content_type and len(bundle) > 0
print("PASS: UI HTML and compiled JavaScript are served")

_, nested_html = get(ui + "/maintenance-requests/compose-smoke-test")
assert nested_html == html, "React Router deep links must serve index.html"
print("PASS: React Router deep links")

for path in ("/api/v1/equipments", "/api/v1/technicians"):
    content_type, proxied = get(ui + path)
    assert "application/json" in content_type, (path, content_type)
    _, direct = get(backend + path)
    assert isinstance(json.loads(proxied), list)
    assert json.loads(proxied) == json.loads(direct), path
    print("PASS: UI proxy -> backend -> MongoDB: " + path)

print("All Compose smoke checks passed.")
