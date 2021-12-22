INSERT users (id, name, password, is_moderator, email, reg_time)
VALUES
('1', 'Петров Петр', '12345', '1', 'email1@mail.ru', '2021-01-01 10:00'),
('2', 'Иванов иван', '56789', '0', 'email2@mail.ru', '2021-01-02 11:00'),
('3', 'Сидоров Сидор', '13579', '0', 'email3@mail.ru', '2021-01-03 12:00');

INSERT posts (id, is_active, moderation_status, moderator_user_id, user_id, time, title, text, view_count)
VALUES
('1', '1', 'NEW', null, '1', '2021-02-01 10:35', 'SQL-запросы', 'Пишем запросы', '0'),
('2', '1', 'ACCEPTED', '1', '2', '2021-02-02 11:36', 'java', 'зачем она нужна', '2'),
('3', '1', 'ACCEPTED', '1', '3', '2021-03-03 1:54', 'С++', 'Неужели все?', '12'),
('4', '1', 'ACCEPTED', '1', '1', '2019-04-04 2:24', 'Spring', 'Профессорская дача на берегу Финского залива. В отсутствие хозяина, друга моего отца, нашей семье позволялось там жить. Даже спустя десятилетия помню, как после утомительной дороги из города меня обволакивала прохлада деревянного дома, как собирала растрясшееся, распавшееся в экипаже тело.', '55'),
('5', '1', 'ACCEPTED', '1', '2', '2021-05-05 12:21', 'Hibernate', ' Текст Текст Текст Текст Текст Текст Текст Текст Текст', '7'),
('6', '1', 'ACCEPTED', '1', '3', '2020-06-01 3:44', 'java', 'Эта прохлада не была связана со свежестью, скорее, как ни странно, — с упоительной затхлостью, в которой слились ароматы старых книг и многочисленных океанских трофеев, непонятно как доставшихся профессору-юристу. Распространяя солоноватый запах, на полках лежали засушенные морские звёзды, перламутровые раковины, резные маски, пробковый шлем и даже игла рыбы-иглы.', '34'),
('7', '1', 'ACCEPTED', '1', '1', '2021-08-08 12:44', 'Hibernate', 'Аккуратно отодвигая дары моря, я доставал с полок книги, садился по-турецки в кресло с самшитовыми подлокотниками и читал. Листал страницы правой рукой, а левая сжимала кусок хлеба с маслом и сахаром. Откусывал задумчиво и читал, и сахар скрипел на моих зубах. Это были жюль-верновские романы или журнальные, переплетённые в кожу описания экзотических стран — мир неведомый, недоступный и от юриспруденции бесконечно далёкий. На своей даче профессор собрал, очевидно, то, о чём ему мечталось с детства, что не предусматривалось его нынешним положением и не регулировалось «Сводом законов Российской империи». В милых его сердцу странах законов, подозреваю, не было вообще.', '55'),
('8', '1', 'ACCEPTED', '1', '2', '2020-01-08 2:44', 'Hadoop', 'Время от времени я поднимал глаза от книги и, наблюдая угасание залива за окном, пытался понять, как становятся юристами. Мечтают об этом с детства? Сомнительно.', '4'),
('9', '1', 'ACCEPTED', '1', '3', '2021-03-08 8:44', 'java', 'отоотот', '8'),
('10', '1', 'ACCEPTED', '1', '1', '2021-08-09 11:44', 'Hadoop', 'отоотот', '90'),
('11', '1', 'ACCEPTED', '1', '1', '2021-04-04 10:44', 'Hibernate', 'отоотот', '7'),
('12', '1', 'ACCEPTED', '1', '2', '2018-08-08 9:44', 'java', 'отоотот', '13'),
('13', '0', 'ACCEPTED', '1', '2', '2017-08-08 12:44', 'Spring', 'отоотот', '43'),
('14', '1', 'ACCEPTED', '1', '3', '2020-03-09 12:44', 'java', 'отоотот', '1'),
('15', '1', 'DECLINED', '1', '3', '2021-01-11 12:44', 'Spring', 'отоотот', '2');

INSERT tags (id, name)
VALUES
('1', 'Java'),
('2', 'Hadoop'),
('3', 'Hibernate'),
('4', 'Spring');

INSERT post_comments (id, parent_id, post_id, user_id, time, text)
VALUES
('1', null, '2', '1', '2021-02-01 10:35', 'Спасибо за интересный пост'),
('2', null, '1', '2', '2021-03-03 11:45', 'Ниачом'),
('3', null, '3', '3', '2021-04-04 12:21', 'Не все понятно, но интересно');

INSERT tag2post (id, post_id, tag_id)
VALUES
('1', '1', '1'),
('2', '2', '1'),
('3', '3', '2'),
('4', '3', '2'),
('5', '4', '3'),
('6', '5', '4'),
('7', '5', '4'),
('8', '6', '1'),
('9', '7', '1');

INSERT posts_votes(id,time,value,post_id,user_id)
VALUES
('1','2021-11-11 10:40','-1','2','1'),
('2','2021-11-11 10:40','-1','6','2'),
('3','2021-11-11 10:40','1','3','3'),
('4','2021-11-11 10:40','-1','4','3'),
('5','2021-11-11 10:40','1','4','2'),
('6','2021-11-11 10:40','1','2','2'),
('7','2021-11-11 10:40','1','2','1');
