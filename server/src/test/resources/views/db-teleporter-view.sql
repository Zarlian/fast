CREATE OR REPLACE VIEW teleporter_settings_details AS
SELECT
    ts.visible,
    tl.*,
    tl.owner AS owner_id,
    tl.location AS teleporter_location,
    teleLoc.longitude,
    teleLoc.latitude,
    ul.longitude AS owner_location_longitude,
    ul.latitude AS owner_location_latitude,
    users.address AS owner_address,
    users.name AS owner_name,
    users.current_location,
    users.profile_picture
FROM
    teleporter_settings ts
        JOIN
    teleporters tl ON ts.teleporter_id = tl.teleporter_id
        JOIN
    users ON tl.owner = users.adria_id
        JOIN
    locations AS ul ON users.current_location = ul.location_id
        JOIN
    locations AS teleLoc ON tl.location = teleLoc.location_id;

CREATE OR REPLACE VIEW user_permissions_details AS
SELECT
    up.permission_id,
    up.can_access_all_logs,
    up.can_manage_lists,
    up.can_assign_admin_permissions,
    up.can_control_teleporters,
    user.adria_id,
    user.name AS user_name,
    user.address AS user_address,
    user.current_location,
    user.profile_picture,
    ul.longitude AS user_location_longitude,
    ul.latitude AS user_location_latitude,
    tl.address,
    tl.teleporter_id,
    tl.name,
    tl.type,
    tl.owner AS owner_id,
    tl.location AS teleporter_location,
    teleLoc.longitude,
    teleLoc.latitude,
    ol.longitude AS owner_location_longitude,
    ol.latitude AS owner_location_latitude,
    owner.address AS owner_address,
    owner.name AS owner_name,
    owner.current_location AS owner_current_location,
    owner.profile_picture AS owner_profile_picture
FROM
    user_permissions up
    JOIN
    users user ON up.adria_id = user.adria_id
    JOIN
    locations AS ul ON user.current_location = ul.location_id
    JOIN
    teleporters tl ON up.teleporter_id = tl.teleporter_id
    JOIN
    users owner ON tl.owner = owner.adria_id
    JOIN
    locations AS ol ON owner.current_location = ol.location_id
    JOIN
    locations AS teleLoc ON tl.location = teleLoc.location_id;