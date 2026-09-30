#!/usr/bin/env python3
"""Compare the bundled CSV seed data with the running Compose MongoDB database."""
import csv
import json
import subprocess
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def rows(name):
    with (ROOT / 'src/main/resources' / name).open(newline='') as source:
        return list(csv.reader(source))[1:]


command = '''const d=db.getSiblingDB("property-management"); print(JSON.stringify({
    equipment:d.equipment.find({}).toArray(),
    inventory:d.equipmentInventory.find({}).toArray(),
    keys:d.unassignedEquipmentKeys.find({}).toArray(),
    marker:d.dataInitializations.findOne({_id:"equipment-day-zero-v1"})
}));'''
data = json.loads(subprocess.check_output(
    ['docker', 'compose', 'exec', '-T', 'mongodb', 'mongosh', '--quiet', '--eval', command],
    cwd=ROOT, text=True))
assert data['marker'], 'Day-zero completion marker is missing'
equipment = {item['tagNumber']: item for item in data['equipment']}
assert len(equipment) == len(data['equipment']), 'Duplicate equipment tags'
for equipment_type, manufacturer, model, tag in rows('equipment-info.csv'):
    item = equipment[tag]
    assert item['equipmentType'] == equipment_type, tag
    assert item['manufacturer'] == manufacturer, tag
    assert (item.get('equipmentModel') or '') == model, tag
    assert item['equipmentDetail']['equipmentStatus'] == 'IN_SERVICE', tag
print('PASS: all 96 master equipment records match their CSV')

for row in rows('equipment-keylist.csv'):
    tag, key, remarks = row[3:6]
    item = equipment[tag] if tag else next(x for x in data['keys']
        if x['equipmentDetail']['keyDetail']['primaryKey'] == key)
    if key:
        assert item['equipmentDetail']['keyDetail']['primaryKey'] == key, tag
    if remarks:
        assert item['equipmentDetail']['equipmentRemarks'] == remarks, tag
print('PASS: all 24 key entries, including the unassigned key, are preserved')

for row in rows('equipment-techicalDetails.csv'):
    tag, serial, front, rear, remarks = row[2], *row[4:8]
    item = equipment[tag]
    if serial:
        assert item['serialNumber'] == serial, tag
    for field, expected in [('frontTirePressure', front), ('backTirePressure', rear), ('equipmentRemarks', remarks)]:
        if expected:
            assert item['equipmentDetail'][field] == expected, (tag, field)
print('PASS: serial numbers, tire specifications, and remarks from all 95 technical rows')

for equipment_type, manufacturer, tag, model, serial, remarks in rows('released-equipment.csv'):
    if tag:
        item = equipment[tag]
    else:
        matches = [x for x in equipment.values() if x['tagNumber'].startswith('IMPORT-RELEASED-')
            and x['equipmentType'] == equipment_type and x['manufacturer'] == manufacturer
            and (x.get('equipmentModel') or '') == model and (x.get('serialNumber') or '') == serial]
        assert len(matches) == 1, (equipment_type, manufacturer, model)
        item = matches[0]
    assert item['equipmentDetail']['equipmentStatus'] == 'RELEASED'
    assert item['equipmentDetail']['equipmentRemarks'] == remarks
print('PASS: all 25 released machines have unique tags and release remarks')

inventory = {item['_id']: item for item in data['inventory']}
for line, (description, quantity) in enumerate(rows('equipment-quantities.csv'), start=2):
    item = inventory['equipment-quantities.csv:' + str(line)]
    assert item['description'] == description
    assert item['quantity'] == int(quantity)
assert sum(item['quantity'] for item in inventory.values()) == 19
print('PASS: all 17 inventory rows, totaling 19 items')
print('PASS: day-zero completion marker exists; equipment count:', len(equipment))
