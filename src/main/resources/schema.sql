
CREATE TABLE public.app_user (
	id bigserial NOT NULL,
	username varchar(40) NOT NULL,
	"password" text NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	CONSTRAINT app_user_pkey PRIMARY KEY (id),
	CONSTRAINT app_user_un UNIQUE (username)
);
CREATE INDEX app_user_id_idx ON public.app_user USING btree (id, username, password, created_at, updated_at);




CREATE TABLE public.user_profile (
	id bigserial NOT NULL,
	first_name varchar(60) NOT NULL,
	last_name varchar(60) NOT NULL,
	dob timestamp NULL,
	gender varchar(60) NULL,
	profile_image varchar(2048) NULL,
	is_enabled bool NOT NULL DEFAULT true,
	user_id int8 NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	CONSTRAINT user_profile_pkey PRIMARY KEY (id)
);
CREATE INDEX user_profile_id_idx ON public.user_profile USING btree (id, first_name, last_name, dob, gender, profile_image, is_enabled, user_id, created_at, updated_at);



-- public.user_profile foreign keys

ALTER TABLE public.user_profile ADD CONSTRAINT user_profile_fkey FOREIGN KEY (user_id) REFERENCES public.app_user(id);




-- public.profile_db_file definition

-- Drop table

-- DROP TABLE public.profile_db_file;

CREATE TABLE public.profile_db_file (
	id bigserial NOT NULL,
	file_name varchar(255) NULL,
	file_type varchar(255) NULL,
	file_content bytea NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	user_id bigserial NOT NULL,
	CONSTRAINT profile_db_file_pkey PRIMARY KEY (id),
	CONSTRAINT profile_db_file_un UNIQUE (file_name)
);
CREATE INDEX profile_db_file_id_idx ON public.profile_db_file USING btree (id, file_name, created_at, updated_at, file_type);


-- public.profile_db_file foreign keys
ALTER TABLE public.profile_db_file ADD CONSTRAINT profile_db_file_fk FOREIGN KEY (user_id) REFERENCES public.app_user(id);




-- public.product definition

-- Drop table

-- DROP TABLE public.product;

CREATE TABLE public.product (
	id bigserial NOT NULL,
	name varchar(40) NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	CONSTRAINT product_pkey PRIMARY KEY (id)
);
CREATE INDEX product_id_idx ON public.product USING btree (id, name, created_at, updated_at);