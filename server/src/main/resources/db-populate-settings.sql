INSERT INTO teleporter_settings (teleporter_id, visible)
SELECT teleporter_id, true
FROM teleporters;