CREATE OR REPLACE VIEW favourites_details AS
SELECT
    f.type,
    f.name,
    u.name AS user_name,
    u.adria_id,
    u.address AS user_address,
    u.profile_picture,
    u.current_location,
    ul.longitude AS user_location_longitude,
    ul.latitude AS user_location_latitude,
    t.name AS teleporter_name,
    t.teleporter_id,
    t.address AS teleporter_address,
    t.type AS teleporter_type,
    t.location AS teleporter_location,
    tl.longitude AS teleporter_location_lon,
    tl.latitude AS teleporter_location_lat,
    t.owner AS owner_id,
    tu.name AS owner_name,
    tu.address AS owner_address,
    tu.profile_picture AS owner_profile_picture,
    tu.current_location AS owner_current_location,
    tul.longitude AS owner_location_longitude,
    tul.latitude AS owner_location_latitude
FROM
    favourites f
JOIN users u ON f.adria_id = u.adria_id
JOIN locations ul ON u.current_location = ul.location_id
JOIN teleporters t ON f.teleporter_id = t.teleporter_id
JOIN users tu ON t.owner = tu.adria_id
JOIN locations tul ON tu.current_location = tul.location_id
JOIN locations tl ON t.location = tl.location_id