drop table if exists captcha_codes;
drop table if exists global_settings;
drop table if exists post_comments;
drop table if exists posts;
drop table if exists posts_votes;
drop table if exists tag2post;
drop table if exists tags;
drop table if exists users;
create table captcha_codes (
 id integer not null auto_increment,
 code TINYTEXT not null,
 secret_code TINYTEXT not null,
 time datetime not null,
 primary key (id));
create table global_settings (
 id integer not null auto_increment,
 code varchar(255) not null,
 name varchar(255) not null,
 value varchar(255) not null,
 primary key (id));
create table post_comments (
 id integer not null auto_increment,
 text varchar(255) not null,
 time datetime not null,
 parent_id integer,
 post_id integer,
 user_id integer,
 primary key (id));
create table posts (
 id integer not null auto_increment,
 is_active TINYINT(1) not null,
 moderation_status varchar(255) not null,
 text text not null,
 time datetime not null,
 title varchar(255) not null,
 view_count integer not null,
 moderator_user_id integer,
 user_id integer,
 primary key (id));
create table posts_votes (
 id integer not null auto_increment,
 time datetime not null,
 value TINYINT(1) not null,
 post_id integer,
 user_id integer,
 primary key (id));
create table tag2post (
 id integer not null auto_increment,
 post_id integer not null,
 tag_id integer not null,
 primary key (id));
create table tags (
 id integer not null auto_increment,
 name varchar(255) not null,
 primary key (id));
create table users (
 id integer not null auto_increment,
 code varchar(255),
 email varchar(255) not null,
 is_moderator TINYINT(1) not null,
 name varchar(255) not null,
 password varchar(255) not null,
 photo TEXT,
 reg_time datetime not null,
 primary key (id));
