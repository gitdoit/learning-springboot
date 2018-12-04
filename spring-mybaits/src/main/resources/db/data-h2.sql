DELETE FROM user;
DELETE FROM role;

INSERT INTO user (id, name, age, email, grade, gender,role_id) VALUES
(2, 'Jack', 3, 'test2@baomidou.com', 1, 0,1),
(3, 'Tom', 1, 'test3@baomidou.com', 2, 1,2),
(1, 'Billie', 2, 'test5@baomidou.com', 3, null,3);

INSERT INTO role (id, role_name, role_describe)
VALUES (1, '管理员', 'boos 级别'),
       (2, '用户', '就是个普通人'),
       (3, '程序猿', '偶尔需要用来祭天');