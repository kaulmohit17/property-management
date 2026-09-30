# Property management

## Run the backend and UI with Docker Compose

Requires Docker Desktop (running) with Docker Compose v2. Keep the repositories
next to each other:

```text
tsc/
  property-management/       # this repository
  property-management-ui/    # React UI, including package-lock.json
```

From this directory:

```sh
docker compose up --build -d --wait
```

- UI: http://localhost:3000
- Backend API: http://localhost:8080/api/v1/equipments

The UI is built with Node and served by Nginx. Requests to `/api/` on the UI
are forwarded to `backend:8080` within the Compose network. The backend uses
`mongodb:27017/property-management`. MongoDB has a persistent named volume
and is not exposed on a host port. Health checks delay dependent services
until their dependencies are ready. The backend image build runs Gradle tests.

The UI Dockerfile and Nginx configuration are maintained in this repository;
the existing UI development-server Dockerfile is not used by Compose.

## Day-zero equipment data

Compose enables `APP_SEED_ENABLED=true`. On the first startup, a Spring Batch
job loads the bundled CSV files into MongoDB before backend readiness becomes
healthy and the frontend starts:

- `equipment-info.csv`: 96 current machines; the master source for names/models.
- `equipment-keylist.csv`: key details merged by tag; the one entry without a
  tag is preserved in `unassignedEquipmentKeys` rather than assigned to a machine.
- `equipment-techicalDetails.csv`: 95 rows of serial numbers, tire specifications,
  and remarks merged by tag. The existing filename spelling is intentional.
- `released-equipment.csv`: 25 released machines, with their release remarks.
  The 15 without tags receive stable `IMPORT-RELEASED-...` identifiers for UI
  navigation. These are generated import identifiers, not physical asset tags.
- `equipment-quantities.csv`: 17 workshop inventory rows totaling 19 items,
  preserved in `equipmentInventory`. This file has no machine tags; the current
  equipment UI does not display this separate inventory collection.

The equipment UI will list 121 machines, including released equipment. Key
and tire details are stored in MongoDB; the current UI shows the basic equipment
fields exposed by its existing API. Tire specifications use the existing
`frontTirePressure`/`backTirePressure` fields. Key identifiers retain their full
source text, including spare-key notes.

Some supplemental names/models conflict with the master list (for example
`BL-02` and `AT-23`). The importer preserves the master names/models and joins
supplemental details using the CSV tags; it does not guess corrected assignments.
The CSV files remain available for reviewing these source inconsistencies.

A successful import stores `equipment-day-zero-v1` in `dataInitializations`.
Subsequent starts skip the import, preserving edits and avoiding duplicate data.
Failed imports prevent startup readiness and do not create this marker; after
fixing the cause, restarting safely retries using tag-based upserts and stable
IDs for untagged entries. The marker lives in MongoDB, so it survives container
recreation along with the equipment. Local runs default to seeding disabled;
set `APP_SEED_ENABLED=true` to enable it outside Compose.

Inspect the import and resulting data with:

```sh
python3 scripts/verify-day-zero.py
docker compose logs backend | grep -E 'Day-zero|Step:|Job:'
docker compose exec mongodb mongosh property-management --eval 'db.equipment.countDocuments({})'
```

## Verify and manage the stack

```sh
docker compose ps
python3 scripts/smoke-test.py
docker compose logs -f backend frontend
docker compose down
```

The smoke test checks HTML, JavaScript assets, React Router deep links, and
equipment/technician API responses through both the UI proxy and the backend.
It does not modify database records. It requires Python 3 on the host.

`docker compose down` preserves MongoDB data. `docker compose down -v` also
deletes this stack's database volume; only use it when you intend to reset data.

To use different host ports, set the same variables for startup and checks:

```sh
export UI_PORT=3001 BACKEND_PORT=8081
docker compose up --build -d --wait
python3 scripts/smoke-test.py
```

Re-run the startup command after changing either project's source to rebuild
the images. For backend tests without Docker, use `./gradlew build` with JDK 11
or 17. The unused ksqlDB client dependency was removed because it could not be
resolved from the configured Maven Central repository.
