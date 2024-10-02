/* FRIENDLIST */

INSERT INTO groups (leader_id, name) VALUES (1, 'Friends');
INSERT INTO groups (leader_id, name) VALUES (2, 'Friends');

/* OTHER GROUPS */

INSERT INTO groups (leader_id, name) VALUES
    (1, 'Colleagues'),
    (1, 'Family'),
    (1, 'Other');

INSERT INTO group_members (group_id, adria_id) VALUES
    (1, 3),
    (1, 4),
    (1, 5),
    (3, 3),
    (3, 4),
    (3, 5),
    (4, 2),
    (4, 3),
    (4, 4),
    (4, 5);