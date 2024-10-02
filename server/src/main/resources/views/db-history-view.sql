CREATE OR REPLACE VIEW trip_details AS
SELECT
    trip.trip_id,
    trip.adria_id,
    trip.group_id,
    trip.from_teleporter_id,
    trip.to_teleporter_id,
    trip.departure,
    trip.arrival,
    grp.name AS group_name,
    group_leader.adria_id AS leader_id,
    group_leader.name AS leader_name,
    group_leader.address AS leader_address,
    group_leader.profile_picture AS leader_profile_picture,
    group_leader.current_location AS leader_current_location,
    group_leader_location.longitude AS leader_location_lon,
    group_leader_location.latitude AS leader_location_lat,
    user.adria_id AS owner_id,
    user.name AS owner_name,
    user.address AS owner_address,
    user.profile_picture AS owner_profile_picture,
    user.current_location AS owner_current_location,
    user_location.longitude AS owner_location_longitude,
    user_location.latitude AS owner_location_latitude,
    from_teleporter.name AS from_teleporter_name,
    from_teleporter.address AS from_teleporter_address,
    from_teleporter.type AS from_teleporter_type,
    from_teleporter.location AS from_teleporter_location,
    from_teleporter_location.longitude AS from_teleporter_location_longitude,
    from_teleporter_location.latitude AS from_teleporter_location_latitude,
    from_teleporter_owner.adria_id AS from_teleporter_owner_id,
    from_teleporter_owner.name AS from_teleporter_owner_name,
    from_teleporter_owner.address AS from_teleporter_owner_address,
    from_teleporter_owner.profile_picture AS from_teleporter_owner_profile_picture,
    from_teleporter_owner.current_location AS from_teleporter_owner_current_location,
    from_teleporter_owner_location.longitude AS from_teleporter_owner_location_longitude,
    from_teleporter_owner_location.latitude AS from_teleporter_owner_location_latitude,
    to_teleporter.name AS to_teleporter_name,
    to_teleporter.address AS to_teleporter_address,
    to_teleporter.type AS to_teleporter_type,
    to_teleporter.location AS to_teleporter_location,
    to_teleporter_location.longitude AS to_teleporter_location_longitude,
    to_teleporter_location.latitude AS to_teleporter_location_latitude,
    to_teleporter_owner.adria_id AS to_teleporter_owner_id,
    to_teleporter_owner.name AS to_teleporter_owner_name,
    to_teleporter_owner.address AS to_teleporter_owner_address,
    to_teleporter_owner.profile_picture AS to_teleporter_owner_profile_picture,
    to_teleporter_owner.current_location AS to_teleporter_owner_current_location,
    to_teleporter_owner_location.longitude AS to_teleporter_owner_location_longitude,
    to_teleporter_owner_location.latitude AS to_teleporter_owner_location_latitude
FROM
    trips AS trip
        JOIN
    users AS user ON trip.adria_id = user.adria_id
LEFT JOIN
    groups AS grp ON (CASE WHEN trip.group_id IS NOT NULL THEN trip.group_id = grp.group_id ELSE grp.group_id IS NULL END)
LEFT JOIN
    users AS group_leader ON grp.leader_id = group_leader.adria_id
LEFT JOIN
    locations AS group_leader_location ON group_leader.current_location = group_leader_location.location_id
JOIN
    locations AS user_location ON user.current_location = user_location.location_id
JOIN
    teleporters AS from_teleporter ON trip.from_teleporter_id = from_teleporter.teleporter_id
JOIN
    teleporters AS to_teleporter ON trip.to_teleporter_id = to_teleporter.teleporter_id
JOIN
    locations AS from_teleporter_location ON from_teleporter.location = from_teleporter_location.location_id
JOIN
    locations AS to_teleporter_location ON to_teleporter.location = to_teleporter_location.location_id
JOIN
    users AS from_teleporter_owner ON from_teleporter.owner = from_teleporter_owner.adria_id
JOIN
    users AS to_teleporter_owner ON to_teleporter.owner = to_teleporter_owner.adria_id
JOIN
    locations AS from_teleporter_owner_location ON from_teleporter_owner.current_location = from_teleporter_owner_location.location_id
JOIN
    locations AS to_teleporter_owner_location ON to_teleporter_owner.current_location = to_teleporter_owner_location.location_id