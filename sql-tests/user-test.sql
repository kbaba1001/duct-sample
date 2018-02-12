BEGIN;
SELECT plan(2);

SELECT has_table('users');

INSERT INTO users (name, email) VALUES ('kbaba', 'kbaba@example.com');

SELECT results_eq(
  $$
    SELECT
      name::text,
      email::text
    FROM
      users
    WHERE
      name = 'kbaba'
  $$,
  $$VALUES
    ('kbaba', 'kbaba@example.com')
  $$,
  'find users'
);

SELECT * FROM finish();
ROLLBACK;
