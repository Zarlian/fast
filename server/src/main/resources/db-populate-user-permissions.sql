INSERT INTO user_permissions (can_access_all_logs,
                              can_manage_lists,
                              can_assign_admin_permissions,
                              can_control_teleporters,
                              teleporter_id,
                              adria_id) VALUES
    (true, true, true, true, 1, 1),
    (true, true, false, false, 1, 2),
    (true, false, false, false, 1, 3),
    (false, false, false, false, 1, 4);