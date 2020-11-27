create database if not exists `hinny-security` default character set = utf8;
use `hinny-security`;


/* ====================================================================================================================
    user_domain -- 用户域
==================================================================================================================== */
create table user_domain
(
    id                  bigint          not null        auto_increment                          comment '主键id',
    domain_id
    name                varchar(127)    not null        unique                                  comment '域名称',
    redis_name_space    varchar(63)     not null        unique                                  comment 'Redis前缀',
    description         varchar(511)                                                            comment '说明',
    create_at           datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at           datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '用户域';
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/

/* ====================================================================================================================
    user -- 用户表
==================================================================================================================== */
create table user
(
    id              bigint          not null        auto_increment                          comment '主键id',
    uid             varchar(63)     not null        unique                                  comment '用户ID(系统自动生成且不会变化)',
    login_name      varchar(63)     not null        unique                                  comment '用户登录名',
    password        varchar(127)                                                            comment '密码',
    telephone       varchar(31)                     unique                                  comment '手机号',
    email           varchar(63)                     unique                                  comment '邮箱',
    expired_time    datetime(3)                                                             comment '帐号过期时间(空表示永不过期)',
    enabled         int(1)          not null        default 1                               comment '是否启用，0：禁用；1：启用',
    nickname        varchar(31)     not null                                                comment '用户昵称',
    avatar          varchar(511)                                                            comment '用户头像',
    regist_channel  int(1)          not null        default 0                               comment '用户注册渠道，0：管理员；1：PC-Web；2：PC-H5；3：IOS-APP；4：Android-APP；5：微信小程序',
    from_source     int(1)          not null        default 0                               comment '用户来源，0：系统注册，1：外部导入(同步)',
    description     varchar(511)                                                            comment '说明',
    create_at       datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at       datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '用户表';
create index user_nickname on user (nickname);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    role -- 角色表
==================================================================================================================== */
create table role
(
    id              bigint          not null        auto_increment                          comment '主键id',
    name            varchar(63)     not null        unique                                  comment '角色名称',
    description     varchar(1023)                                                           comment '角色说明',
    create_at       datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at       datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '角色表';
create index role_name on role (name);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/


/* ====================================================================================================================
    permission -- 权限表
==================================================================================================================== */
create table permission
(
    id              bigint          not null        auto_increment                          comment '主键id',
    user_domain     varchar(127)    not null                                                comment '权限所属域',
    title           varchar(255)    not null                                                comment '权限标题',
    permission_str  varchar(255)    not null        unique                                  comment '唯一权限标识字符串',
    resources_type  int(1)          not null        default 1                               comment '权限类型，1:API接口, 2:菜单权限，3:ui权限，......',
    description     varchar(1203)                                                           comment '权限说明',
    create_at       datetime(3)     not null        default current_timestamp(3)            comment '创建时间',
    update_at       datetime(3)                     on update current_timestamp(3)          comment '更新时间',
    primary key (id)
) comment = '权限表';
create index permission_sys_name on permission (sys_name);
create index permission_permission_str on permission (permission_str);
/*------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------------------*/









































