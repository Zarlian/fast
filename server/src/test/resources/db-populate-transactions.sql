INSERT INTO transactions (type, name, price, max_uses) VALUES
    ('TICKET', 'Single ticket', 2.0, 1),
    ('SUBSCRIPTION', 'Limited', 70.0, 40),
    ('SUBSCRIPTION', 'Unlimited', 200.0, null);

INSERT INTO user_transactions (adria_id, transact_id, uses_left) VALUES
    (1, 1, 1),
    (1, 1, 1),
    (1, 1, 1),
    (2, 2, 40),
    (3, 2, 25),
    (4, 2, 40),
    (5, 3, null);