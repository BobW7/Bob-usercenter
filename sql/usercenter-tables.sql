-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    userAccount  varchar(256)                       not null comment '用户账号',
    username     varchar(256)                       null comment '昵称',
    avatarUrl    varchar(1024)                      null comment '头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话号码',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '用户状态',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     int      default 0                 null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员 2 - VIP
'
)
    comment '用户表';

