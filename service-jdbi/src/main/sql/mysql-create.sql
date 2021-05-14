create table if not exists `locus` (
  `locus_id` bigint not null auto_increment,
  `name` varchar(128) not null,
  `uri` varchar(512) not null,
  primary key (`locus_id`),
  index (`name`),
  index (`uri`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `term` (
  `term_id` bigint not null auto_increment,
  `name` varchar(256) not null,
  `accession` varchar(128) not null,
  `uri` varchar(512) not null,
  primary key (`term_id`),
  index (`name`),
  index (`accession`),
  index (`uri`)
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `sequence` (
  `sequence_id` bigint not null auto_increment,
  `sequence` mediumtext not null,
  primary key (`sequence_id`),
  index (`sequence`(256))
) engine=innodb charset=latin1 auto_increment=1;

create table if not exists `feature` (
  feature_id bigint not null auto_increment,
  `locus_id` bigint not null,
  `term_id` bigint not null, 
  `rank` integer not null,
  `sequence_id` bigint not null,
  `accession` bigint not null,
  primary key (`feature_id`),
  unique key `unique_feature` (`locus_id`,`term_id`,`rank`,`accession`),
  index feature_sequence (`locus_id`,`term_id`,`rank`,`sequence_id`),
  constraint foreign key (`locus_id`) references locus (`locus_id`),
  constraint foreign key (`term_id`) references term (`term_id`),
  constraint foreign key (`sequence_id`) references sequence (`sequence_id`)
) engine=innodb charset=latin1 auto_increment=1;
