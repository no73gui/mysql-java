DROP TABLE project_category IF EXISTS;
DROP TABLE material IF EXISTS;
DROP TABLE step IF EXISTS;
DROP TABLE category IF EXISTS;
DROP TABLE project IF EXISTS;

CREATE TABLE projects (
	project_id INT NOT NULL AUTO_INCREMENT ,
	project_name VARCHAR(128) , 
	estimated_hours DECIMAL(7 , 2) , 
	actual_hours DECIMAL(7 , 2) , 
	difficulty INT ,
	notes TEXT ,
	PRIMARY KEY (project_id)
);

CREATE TABLE category (
	category_id INT NOT NULL AUTO_INCREMENT ,
	category_name VARCHAR(128),
	PRIMARY KEY (category_id)
);

CREATE TABLE step (
	step_id INT NOT NULL AUTO_INCREMENT ,
	project_id INT NOT NULL ,
	step_text TEXT NOT NULL ,
	step_order INT ,
	PRIMARY KEY (step_id) , 
	FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE
);

CREATE TABLE material (
	material_id INT NOT NULL AUTO_INCREMENT ,
	project_id INT NOT NULL , 
	material_name VARCHAR(128) NOT NULL,
	num_required INT ,
	cost DECIMAL(7 , 2) ,
	PRIMARY KEY (material_id) ,
	FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE
);

CREATE TABLE project_category (
	project_id INT NOT NULL ,
	category_id INT NOT NULL ,
	FOREIGN KEY (project_id) REFERENCES project(project_id) ON DELETE CASCADE ,
	FOREIGN KEY (category_id) REFERENCES category(category_id)
);


