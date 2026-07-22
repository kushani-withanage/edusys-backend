-- =====================================================================
-- EduSys Sample Data Seeding
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. Seeding Career Levels
-- ---------------------------------------------------------------------
INSERT INTO career_levels (level_id, level_name, description, min_points, max_points) VALUES
('lvl0001', 'L1: Explorer', 'Getting started with basic programming fundamentals, CLI tools, and version control foundations.', 0, 99),
('lvl0002', 'L2: Builder', 'Capable of constructing responsive user interfaces, simple web applications, and styling systems.', 100, 299),
('lvl0003', 'L3: Developer', 'Proficient in writing full stack CRUD interfaces, working with relational databases, and integrating basic third-party APIs.', 300, 599),
('lvl0004', 'L4: Engineer', 'Skilled at architectural design, writing test suites, optimizing performance, and handling application authentication flows.', 600, 999),
('lvl0005', 'L5: Architect', 'Capable of designing highly scalable microservices, complex cloud architectures, and mentoring junior developers.', 1000, 1499),
('lvl0006', 'L6: Lead', 'Leads development teams, establishes CI/CD pipelines, enforces code quality guidelines, and leads high-impact features.', 1500, 1999),
('lvl0007', 'L7: Master', 'Endeavor in software engineering, designs complex frameworks, and makes major strategic technical decisions.', 2000, 9999);

-- ---------------------------------------------------------------------
-- 2. Seeding Courses (Modules)
-- ---------------------------------------------------------------------
INSERT INTO courses (course_id, course_name, credits, duration_weeks, description) VALUES
('crs0001', 'Programming Fundamentals', 3, 12, 'Introduction to algorithmic structures, variable mappings, logic, loops, arrays, and problem-solving structures.'),
('crs0002', 'Object Oriented Programming', 4, 16, 'Encapsulation, inheritance, polymorphism, abstract class overrides, design patterns, and Java syntax standards.'),
('crs0003', 'Internet Technologies', 3, 12, 'HTTP protocols, REST API architectures, client-server handshake, web security standards, and responsive web configurations.'),
('crs0004', 'Standalone Application (JavaFX)', 4, 16, 'Desktop client application development, event-driven listener structures, local storage, and multithreading processes.'),
('crs0005', 'Enterprise Engineering', 4, 24, 'Distributed architectures, microservices, cloud deployments, message queue brokers, and automated CI/CD pipelines.'),
('crs0006', 'Database Management Systems', 4, 18, 'Relational query design schemas, SQL query execution plans, normalization rules, indexes, and ACID transactions.');

-- ---------------------------------------------------------------------
-- 3. Seeding Batches
-- ---------------------------------------------------------------------
INSERT INTO batches (batch_id, batch_name, start_date, end_date) VALUES
('bat0001', 'iCD110', '2026-01-15', '2026-07-15'),
('bat0002', 'iCM111', '2026-02-15', '2026-08-15'),
('bat0003', 'iCD112', '2026-03-15', '2026-09-15');

-- ---------------------------------------------------------------------
-- 4. Seeding Batch Course Association
-- ---------------------------------------------------------------------
INSERT INTO batch_course (batch_id, course_id) VALUES
('bat0001', 'crs0001'), ('bat0001', 'crs0002'), ('bat0001', 'crs0003'), ('bat0001', 'crs0006'),
('bat0002', 'crs0001'), ('bat0002', 'crs0003'), ('bat0002', 'crs0004'),
('bat0003', 'crs0001'), ('bat0003', 'crs0003'), ('bat0003', 'crs0005'), ('bat0003', 'crs0006');

-- ---------------------------------------------------------------------
-- 5. Seeding Teacher (Sharadha Marasinghe)
-- ---------------------------------------------------------------------
INSERT INTO users (user_id, full_name, email, role, phone, password, status, created_at) VALUES
('usr0001', 'Sharadha Marasinghe', 'sharadha@edusys.com', 'TEACHER', '+94771234567', '$2b$10$AgoVrpMeGpWc73R0bfx9yu9N.8arSNL86Jgn0USpqXu6ORrwtYam2', 'ACTIVE', NOW());

INSERT INTO teachers (teacher_id, specialization, join_date) VALUES
('usr0001', 'Software Engineering', '2025-01-10');

-- ---------------------------------------------------------------------
-- 6. Seeding Students (5 Sri Lankan Students)
-- ---------------------------------------------------------------------
INSERT INTO users (user_id, full_name, email, role, phone, password, status, created_at) VALUES
('usr0002', 'Sachin Samarawickrama', 'sachin@edusys.com', 'STUDENT', '+94770000001', '$2b$10$yuYQnOpmleDitkLI0WVNIuvVfkDDcEThB2RkO1GqYZr2C2lhf8USa', 'ACTIVE', NOW()),
('usr0003', 'Pawara Samarawickrama', 'pawara@edusys.com', 'STUDENT', '+94770000002', '$2b$10$uKW9z.vZphRxO9UQTAnGY.YSztDT5IPuohOLa.z/de8RlhdUDupwy', 'ACTIVE', NOW()),
('usr0004', 'Dinuka Perera', 'dinuka@edusys.com', 'STUDENT', '+94770000003', '$2b$10$n3hZAHByKQHkAAS0igiIU.qZrix0dq2Kkn8MKVnhevWmCIkTNNeRC', 'ACTIVE', NOW()),
('usr0005', 'Kasun Jayasuriya', 'kasun@edusys.com', 'STUDENT', '+94770000004', '$2b$10$CyaVzViNs8hrgfLH1xAa4Oy6doRGXaU/TVOHC4BDBE1ozU2kqodcW', 'ACTIVE', NOW()),
('usr0006', 'Nimali Silva', 'nimali@edusys.com', 'STUDENT', '+94770000005', '$2b$10$OeOPwwCecaHUT99dHUuxjuKQc.xkkk6Y4cCps0auQuhBjGDy5qDeK', 'ACTIVE', NOW());

-- Custom Registration numbers:
-- pr (Physical) + 26 (Year) + Batch Number (e.g. 110/111/112) + Student Index (001/002)
INSERT INTO students (student_id, address, reg_no, enrollment_date, dob) VALUES
('usr0002', '123 Galle Road, Colombo 03', 'pr26110001', '2026-01-15', '2004-05-12'),
('usr0003', '456 Kandy Road, Kadawatha', 'pr26110002', '2026-01-15', '2004-09-22'),
('usr0004', '789 Negombo Road, Kurunegala', 'pr26111001', '2026-02-15', '2005-02-18'),
('usr0005', '101 Horana Road, Panadura', 'pr26112001', '2026-03-15', '2004-11-30'),
('usr0006', '202 High Level Road, Maharagama', 'pr26112002', '2026-03-15', '2005-07-05');

-- ---------------------------------------------------------------------
-- 7. Seeding Student Enrollments
-- ---------------------------------------------------------------------
INSERT INTO enrollments (enrollment_id, student_id, batch_id, course_id, enroll_date) VALUES
('enr0001', 'usr0002', 'bat0001', 'crs0001', '2026-01-15'),
('enr0002', 'usr0002', 'bat0001', 'crs0002', '2026-01-15'),
('enr0003', 'usr0002', 'bat0001', 'crs0003', '2026-01-15'),
('enr0004', 'usr0002', 'bat0001', 'crs0006', '2026-01-15'),

('enr0005', 'usr0003', 'bat0001', 'crs0001', '2026-01-15'),
('enr0006', 'usr0003', 'bat0001', 'crs0002', '2026-01-15'),
('enr0007', 'usr0003', 'bat0001', 'crs0003', '2026-01-15'),
('enr0008', 'usr0003', 'bat0001', 'crs0006', '2026-01-15'),

('enr0009', 'usr0004', 'bat0002', 'crs0001', '2026-02-15'),
('enr0010', 'usr0004', 'bat0002', 'crs0003', '2026-02-15'),
('enr0011', 'usr0004', 'bat0002', 'crs0004', '2026-02-15'),

('enr0012', 'usr0005', 'bat0003', 'crs0001', '2026-03-15'),
('enr0013', 'usr0005', 'bat0003', 'crs0003', '2026-03-15'),
('enr0014', 'usr0005', 'bat0003', 'crs0005', '2026-03-15'),
('enr0015', 'usr0005', 'bat0003', 'crs0006', '2026-03-15'),

('enr0016', 'usr0006', 'bat0003', 'crs0001', '2026-03-15'),
('enr0017', 'usr0006', 'bat0003', 'crs0003', '2026-03-15'),
('enr0018', 'usr0006', 'bat0003', 'crs0005', '2026-03-15'),
('enr0019', 'usr0006', 'bat0003', 'crs0006', '2026-03-15');

-- ---------------------------------------------------------------------
-- 8. Seeding Fee Records and Receipts (Pricing in Rs.)
-- ---------------------------------------------------------------------
-- iCD fee: Rs. 160,000. iCM fee: Rs. 110,000. Random discounts applied directly to amount.
INSERT INTO fee_records (fee_id, student_id, amount, due_date, fee_type, status) VALUES
('fee0001', 'usr0002', 150000.00, '2026-03-01', 'Tuition Fee (iCD110 - Discounted LKR 10,000)', 'PENDING'),
('fee0002', 'usr0003', 145000.00, '2026-03-01', 'Tuition Fee (iCD110 - Discounted LKR 15,000)', 'PENDING'),
('fee0003', 'usr0004', 100000.00, '2026-04-01', 'Tuition Fee (iCM111 - Discounted LKR 10,000)', 'PAID'),
('fee0004', 'usr0005', 160000.00, '2026-05-01', 'Tuition Fee (iCD112)', 'PENDING'),
('fee0005', 'usr0006', 155000.00, '2026-05-01', 'Tuition Fee (iCD112 - Discounted LKR 5,000)', 'UNPAID');

INSERT INTO receipts (receipt_id, receipt_no, fee_id, payment_date, amount_paid, payment_method) VALUES
('rec0001', 'REC-2026-0001', 'fee0001', '2026-02-01', 50000.00, 'Cash'),
('rec0002', 'REC-2026-0002', 'fee0002', '2026-02-05', 80000.00, 'Bank Transfer'),
('rec0003', 'REC-2026-0003', 'fee0003', '2026-03-01', 100000.00, 'Card'),
('rec0004', 'REC-2026-0004', 'fee0004', '2026-03-10', 60000.00, 'Cash');

-- ---------------------------------------------------------------------
-- 9. Seeding Question Bank, Options, and Correct Answers
-- ---------------------------------------------------------------------

-- Module 1: Programming Fundamentals (crs0001)
INSERT INTO question_bank (question_id, question_type, question_text, marks, created_by) VALUES
('qst0001', 'MCQ', 'What is the correct way to declare an integer variable in Java?', 5, 'usr0001'),
('qst0002', 'MCQ', 'Which loop is guaranteed to execute at least once?', 5, 'usr0001'),
('qst0003', 'MCQ', 'What is the logical operator for AND in Java?', 5, 'usr0001'),
('qst0004', 'MCQ', 'Which of the following is not a primitive data type in Java?', 5, 'usr0001'),
('qst0005', 'MCQ', 'How do you start writing a single-line comment in Java?', 5, 'usr0001'),
('qst0006', 'MCQ', 'What is the default value of a boolean variable?', 5, 'usr0001'),
('qst0007', 'MCQ', 'Which statement is used to exit a loop early?', 5, 'usr0001'),
('qst0008', 'MCQ', 'What is the index of the first element in a Java array?', 5, 'usr0001'),
('qst0009', 'MCQ', 'Which operator is used to find the remainder of a division?', 5, 'usr0001'),
('qst0010', 'MCQ', 'Which command is used to compile a Java file from the command line?', 5, 'usr0001');

INSERT INTO question_options (question_id, option_value) VALUES
('qst0001', 'int x;'), ('qst0001', 'float x;'), ('qst0001', 'integer x;'), ('qst0001', 'var int x;'),
('qst0002', 'do-while'), ('qst0002', 'while'), ('qst0002', 'for'), ('qst0002', 'for-each'),
('qst0003', '&&'), ('qst0003', '||'), ('qst0003', '!'), ('qst0003', '&'),
('qst0004', 'String'), ('qst0004', 'int'), ('qst0004', 'char'), ('qst0004', 'double'),
('qst0005', '//'), ('qst0005', '/*'), ('qst0005', '#'), ('qst0005', '<!--'),
('qst0006', 'false'), ('qst0006', 'true'), ('qst0006', 'null'), ('qst0006', '0'),
('qst0007', 'break'), ('qst0007', 'continue'), ('qst0007', 'return'), ('qst0007', 'exit'),
('qst0008', '0'), ('qst0008', '1'), ('qst0008', '-1'), ('qst0008', 'null'),
('qst0009', '%'), ('qst0009', '/'), ('qst0009', '*'), ('qst0009', '&'),
('qst0010', 'javac'), ('qst0010', 'java'), ('qst0010', 'javadoc'), ('qst0010', 'compile');

INSERT INTO question_correct_answers (question_id, correct_answer) VALUES
('qst0001', 'int x;'),
('qst0002', 'do-while'),
('qst0003', '&&'),
('qst0004', 'String'),
('qst0005', '//'),
('qst0006', 'false'),
('qst0007', 'break'),
('qst0008', '0'),
('qst0009', '%'),
('qst0010', 'javac');

-- Module 2: Object Oriented Programming (crs0002)
INSERT INTO question_bank (question_id, question_type, question_text, marks, created_by) VALUES
('qst0011', 'MCQ', 'Which keyword is used to inherit a class in Java?', 5, 'usr0001'),
('qst0012', 'MCQ', 'What is the process of hiding implementation details and showing only functionality?', 5, 'usr0001'),
('qst0013', 'MCQ', 'Which of the following allows a subclass to provide a specific implementation of a method in its superclass?', 5, 'usr0001'),
('qst0014', 'MCQ', 'Which modifier makes a variable accessible only within the same class?', 5, 'usr0001'),
('qst0015', 'MCQ', 'What type of method does not have a body in an abstract class?', 5, 'usr0001'),
('qst0016', 'MCQ', 'How many superclasses can a Java class directly inherit?', 5, 'usr0001'),
('qst0017', 'MCQ', 'Which keyword is used to call a superclass constructor?', 5, 'usr0001'),
('qst0018', 'MCQ', 'What is polymorphism?', 5, 'usr0001'),
('qst0019', 'MCQ', 'Which keyword prevents a class from being subclassed?', 5, 'usr0001'),
('qst0020', 'MCQ', 'Which constructor is called automatically when no constructor is written in a class?', 5, 'usr0001');

INSERT INTO question_options (question_id, option_value) VALUES
('qst0011', 'extends'), ('qst0011', 'implements'), ('qst0011', 'inherits'), ('qst0011', 'exports'),
('qst0012', 'Abstraction'), ('qst0012', 'Encapsulation'), ('qst0012', 'Inheritance'), ('qst0012', 'Polymorphism'),
('qst0013', 'Method Overriding'), ('qst0013', 'Method Overloading'), ('qst0013', 'Method Overhiding'), ('qst0013', 'Method Overwrapping'),
('qst0014', 'private'), ('qst0014', 'public'), ('qst0014', 'protected'), ('qst0014', 'default'),
('qst0015', 'Abstract method'), ('qst0015', 'Static method'), ('qst0015', 'Private method'), ('qst0015', 'Final method'),
('qst0016', '1'), ('qst0016', '2'), ('qst0016', 'Unlimited'), ('qst0016', '0'),
('qst0017', 'super'), ('qst0017', 'this'), ('qst0017', 'parent'), ('qst0017', 'base'),
('qst0018', 'One interface, multiple implementations'), ('qst0018', 'Hiding data fields'), ('qst0018', 'Restricting access to methods'), ('qst0018', 'Splitting a class into files'),
('qst0019', 'final'), ('qst0019', 'const'), ('qst0019', 'static'), ('qst0019', 'abstract'),
('qst0020', 'Default constructor'), ('qst0020', 'Parameterized constructor'), ('qst0020', 'Static constructor'), ('qst0020', 'Copy constructor');

INSERT INTO question_correct_answers (question_id, correct_answer) VALUES
('qst0011', 'extends'),
('qst0012', 'Abstraction'),
('qst0013', 'Method Overriding'),
('qst0014', 'private'),
('qst0015', 'Abstract method'),
('qst0016', '1'),
('qst0017', 'super'),
('qst0018', 'One interface, multiple implementations'),
('qst0019', 'final'),
('qst0020', 'Default constructor');

-- Module 3: Internet Technologies (crs0003)
INSERT INTO question_bank (question_id, question_type, question_text, marks, created_by) VALUES
('qst0021', 'MCQ', 'What does HTML stand for?', 5, 'usr0001'),
('qst0022', 'MCQ', 'Which HTTP status code represents OK?', 5, 'usr0001'),
('qst0023', 'MCQ', 'Which HTTP method is typically used to create a new resource?', 5, 'usr0001'),
('qst0024', 'MCQ', 'What does JSON stand for?', 5, 'usr0001'),
('qst0025', 'MCQ', 'Which port is typically used for secure HTTPS communication?', 5, 'usr0001'),
('qst0026', 'MCQ', 'Which HTTP header is commonly used to send authentication tokens?', 5, 'usr0001'),
('qst0027', 'MCQ', 'What does DOM stand for in web development?', 5, 'usr0001'),
('qst0028', 'MCQ', 'Which CSS property changes the text color?', 5, 'usr0001'),
('qst0029', 'MCQ', 'Which HTTP response code represents Unauthorized?', 5, 'usr0001'),
('qst0030', 'MCQ', 'What is the standard protocol used to fetch data asynchronously?', 5, 'usr0001');

INSERT INTO question_options (question_id, option_value) VALUES
('qst0021', 'HyperText Markup Language'), ('qst0021', 'HighText Machine Language'), ('qst0021', 'HyperTransfer Markup Language'), ('qst0021', 'Hyperlink and Text Management Language'),
('qst0022', '200'), ('qst0022', '404'), ('qst0022', '500'), ('qst0022', '301'),
('qst0023', 'POST'), ('qst0023', 'GET'), ('qst0023', 'PUT'), ('qst0023', 'DELETE'),
('qst0024', 'JavaScript Object Notation'), ('qst0024', 'Java Standard Object Network'), ('qst0024', 'Joint System Object Notation'), ('qst0024', 'JavaScript Online Network'),
('qst0025', '443'), ('qst0025', '80'), ('qst0025', '8080'), ('qst0025', '22'),
('qst0026', 'Authorization'), ('qst0026', 'Authentication'), ('qst0026', 'Token'), ('qst0026', 'User-Agent'),
('qst0027', 'Document Object Model'), ('qst0027', 'Data Object Management'), ('qst0027', 'Domain Object Mapping'), ('qst0027', 'Digital Output Module'),
('qst0028', 'color'), ('qst0028', 'text-color'), ('qst0028', 'font-color'), ('qst0028', 'background-color'),
('qst0029', '401'), ('qst0029', '403'), ('qst0029', '400'), ('qst0029', '405'),
('qst0030', 'AJAX'), ('qst0030', 'FTP'), ('qst0030', 'SMTP'), ('qst0030', 'DNS');

INSERT INTO question_correct_answers (question_id, correct_answer) VALUES
('qst0021', 'HyperText Markup Language'),
('qst0022', '200'),
('qst0023', 'POST'),
('qst0024', 'JavaScript Object Notation'),
('qst0025', '443'),
('qst0026', 'Authorization'),
('qst0027', 'Document Object Model'),
('qst0028', 'color'),
('qst0029', '401'),
('qst0030', 'AJAX');

-- Module 4: Standalone Application (JavaFX) (crs0004)
INSERT INTO question_bank (question_id, question_type, question_text, marks, created_by) VALUES
('qst0031', 'MCQ', 'What is the top-level container in a JavaFX application?', 5, 'usr0001'),
('qst0032', 'MCQ', 'Which component represents the physical contents of a JavaFX window?', 5, 'usr0001'),
('qst0033', 'MCQ', 'Which class is the base class for all nodes in the JavaFX scene graph?', 5, 'usr0001'),
('qst0034', 'MCQ', 'Which layout pane arranges its children in a single horizontal row?', 5, 'usr0001'),
('qst0035', 'MCQ', 'Which tool is used to design JavaFX layouts visually?', 5, 'usr0001'),
('qst0036', 'MCQ', 'What is the extension of files generated by Scene Builder?', 5, 'usr0001'),
('qst0037', 'MCQ', 'How do you register an event handler on a Button in JavaFX?', 5, 'usr0001'),
('qst0038', 'MCQ', 'Which JavaFX thread is responsible for updating the UI?', 5, 'usr0001'),
('qst0039', 'MCQ', 'Which CSS-like styling extension is used in JavaFX?', 5, 'usr0001'),
('qst0040', 'MCQ', 'Which property binding mode synchronizes two properties bidirectionally?', 5, 'usr0001');

INSERT INTO question_options (question_id, option_value) VALUES
('qst0031', 'Stage'), ('qst0031', 'Scene'), ('qst0031', 'Pane'), ('qst0031', 'Window'),
('qst0032', 'Scene'), ('qst0032', 'Stage'), ('qst0032', 'Root'), ('qst0032', 'Layout'),
('qst0033', 'Node'), ('qst0033', 'Parent'), ('qst0033', 'Control'), ('qst0033', 'Shape'),
('qst0034', 'HBox'), ('qst0034', 'VBox'), ('qst0034', 'GridPane'), ('qst0034', 'StackPane'),
('qst0035', 'Scene Builder'), ('qst0035', 'JavaFX Studio'), ('qst0035', 'Swing Designer'), ('qst0035', 'WindowBuilder'),
('qst0036', '.fxml'), ('qst0036', '.xml'), ('qst0036', '.jfx'), ('qst0036', '.javafx'),
('qst0037', 'setOnAction()'), ('qst0037', 'setOnClick()'), ('qst0037', 'setEventListener()'), ('qst0037', 'addButtonListener()'),
('qst0038', 'JavaFX Application Thread'), ('qst0038', 'Main Thread'), ('qst0038', 'UI Event Thread'), ('qst0038', 'Background Worker Thread'),
('qst0039', 'JavaFX CSS'), ('qst0039', 'Sass'), ('qst0039', 'JCSS'), ('qst0039', 'Tailwind'),
('qst0040', 'bindBidirectional()'), ('qst0040', 'bind()'), ('qst0040', 'bindDouble()'), ('qst0040', 'bindTwoWay()');

INSERT INTO question_correct_answers (question_id, correct_answer) VALUES
('qst0031', 'Stage'),
('qst0032', 'Scene'),
('qst0033', 'Node'),
('qst0034', 'HBox'),
('qst0035', 'Scene Builder'),
('qst0036', '.fxml'),
('qst0037', 'setOnAction()'),
('qst0038', 'JavaFX Application Thread'),
('qst0039', 'JavaFX CSS'),
('qst0040', 'bindBidirectional()');

-- Module 5: Enterprise Engineering (crs0005)
INSERT INTO question_bank (question_id, question_type, question_text, marks, created_by) VALUES
('qst0041', 'MCQ', 'What is a microservices architecture?', 5, 'usr0001'),
('qst0042', 'MCQ', 'Which tool is widely used to containerize enterprise applications?', 5, 'usr0001'),
('qst0043', 'MCQ', 'What is the primary purpose of a CI/CD pipeline?', 5, 'usr0001'),
('qst0044', 'MCQ', 'Which component acts as a message broker in event-driven systems?', 5, 'usr0001'),
('qst0045', 'MCQ', 'In cloud engineering, what does horizontal scaling mean?', 5, 'usr0001'),
('qst0046', 'MCQ', 'What does REST stand for?', 5, 'usr0001'),
('qst0047', 'MCQ', 'Which technology is typically used to orchestrate containerized microservices?', 5, 'usr0001'),
('qst0048', 'MCQ', 'What is the purpose of an API Gateway?', 5, 'usr0001'),
('qst0049', 'MCQ', 'Which status code represents an internal server error?', 5, 'usr0001'),
('qst0050', 'MCQ', 'What does JWT stand for in microservices security?', 5, 'usr0001');

INSERT INTO question_options (question_id, option_value) VALUES
('qst0041', 'Decomposing an application into small, independent services'), ('qst0041', 'Running an application on a single large server'), ('qst0041', 'Developing everything in a single modular code base'), ('qst0041', 'Using a single shared database for all services'),
('qst0042', 'Docker'), ('qst0042', 'Jenkins'), ('qst0042', 'Kubernetes'), ('qst0042', 'Git'),
('qst0043', 'To automate testing, building, and deploying code'), ('qst0043', 'To write code comments automatically'), ('qst0043', 'To manage database schemas locally'), ('qst0043', 'To control user permissions manually'),
('qst0044', 'Apache Kafka'), ('qst0044', 'MySQL'), ('qst0044', 'Nginx'), ('qst0044', 'Docker Compose'),
('qst0045', 'Adding more instances of a service'), ('qst0045', 'Increasing the CPU and RAM of an existing server'), ('qst0045', 'Migrating databases to a different provider'), ('qst0045', 'Reducing database normalization levels'),
('qst0046', 'Representational State Transfer'), ('qst0046', 'Remote Encryption Standard Technology'), ('qst0046', 'Relational State Transaction'), ('qst0046', 'Real-time State Transfer'),
('qst0047', 'Kubernetes'), ('qst0047', 'Docker Desktop'), ('qst0047', 'VirtualBox'), ('qst0047', 'Maven'),
('qst0048', 'To act as a single entry point routing requests to microservices'), ('qst0048', 'To store user profile details in cache'), ('qst0048', 'To compile Java source files in deployment'), ('qst0048', 'To secure connections to database servers'),
('qst0049', '500'), ('qst0049', '400'), ('qst0049', '503'), ('qst0049', '404'),
('qst0050', 'JSON Web Token'), ('qst0050', 'Java Web Technology'), ('qst0050', 'Joint Web Trust'), ('qst0050', 'JSON Work Transfer');

INSERT INTO question_correct_answers (question_id, correct_answer) VALUES
('qst0041', 'Decomposing an application into small, independent services'),
('qst0042', 'Docker'),
('qst0043', 'To automate testing, building, and deploying code'),
('qst0044', 'Apache Kafka'),
('qst0045', 'Adding more instances of a service'),
('qst0046', 'Representational State Transfer'),
('qst0047', 'Kubernetes'),
('qst0048', 'To act as a single entry point routing requests to microservices'),
('qst0049', '500'),
('qst0050', 'JSON Web Token');

-- Module 6: Database Management Systems (crs0006)
INSERT INTO question_bank (question_id, question_type, question_text, marks, created_by) VALUES
('qst0051', 'MCQ', 'What is the SQL keyword used to retrieve data from a table?', 5, 'usr0001'),
('qst0052', 'MCQ', 'Which normal form ensures all non-key columns depend transitively on the primary key?', 5, 'usr0001'),
('qst0053', 'MCQ', 'What does ACID stand for in relational databases?', 5, 'usr0001'),
('qst0054', 'MCQ', 'Which SQL clause is used to filter records in a group?', 5, 'usr0001'),
('qst0055', 'MCQ', 'What type of key uniquely identifies a row in a table?', 5, 'usr0001'),
('qst0056', 'MCQ', 'Which command removes all records from a table without logging row deletions?', 5, 'usr0001'),
('qst0057', 'MCQ', 'What is an index used for in databases?', 5, 'usr0001'),
('qst0058', 'MCQ', 'Which SQL join returns all rows from the left table and matched rows from the right table?', 5, 'usr0001'),
('qst0059', 'MCQ', 'What constraint prevents null values in a column?', 5, 'usr0001'),
('qst0060', 'MCQ', 'What SQL command adds a new column to an existing table?', 5, 'usr0001');

INSERT INTO question_options (question_id, option_value) VALUES
('qst0051', 'SELECT'), ('qst0051', 'GET'), ('qst0051', 'FETCH'), ('qst0051', 'EXTRACT'),
('qst0052', 'Third Normal Form (3NF)'), ('qst0052', 'First Normal Form (1NF)'), ('qst0052', 'Second Normal Form (2NF)'), ('qst0052', 'Boyce-Codd Normal Form (BCNF)'),
('qst0053', 'Atomicity, Consistency, Isolation, Durability'), ('qst0053', 'Access, Connection, Indexing, Delivery'), ('qst0053', 'Allocation, Concurrency, Integrity, Distribution'), ('qst0053', 'Automation, Compilation, Integration, Deployment'),
('qst0054', 'HAVING'), ('qst0054', 'WHERE'), ('qst0054', 'GROUP BY'), ('qst0054', 'FILTER'),
('qst0055', 'Primary Key'), ('qst0055', 'Foreign Key'), ('qst0055', 'Unique Key'), ('qst0055', 'Composite Key'),
('qst0056', 'TRUNCATE'), ('qst0056', 'DELETE'), ('qst0056', 'DROP'), ('qst0056', 'REMOVE'),
('qst0057', 'To speed up data retrieval operations'), ('qst0057', 'To ensure data types are consistent'), ('qst0057', 'To link two unrelated tables'), ('qst0057', 'To encrypt sensitive columns'),
('qst0058', 'LEFT JOIN'), ('qst0058', 'RIGHT JOIN'), ('qst0058', 'INNER JOIN'), ('qst0058', 'FULL JOIN'),
('qst0059', 'NOT NULL'), ('qst0059', 'UNIQUE'), ('qst0059', 'CHECK'), ('qst0059', 'DEFAULT'),
('qst0060', 'ALTER TABLE'), ('qst0060', 'UPDATE TABLE'), ('qst0060', 'MODIFY TABLE'), ('qst0060', 'ADD COLUMN');

INSERT INTO question_correct_answers (question_id, correct_answer) VALUES
('qst0051', 'SELECT'),
('qst0052', 'Third Normal Form (3NF)'),
('qst0053', 'Atomicity, Consistency, Isolation, Durability'),
('qst0054', 'HAVING'),
('qst0055', 'Primary Key'),
('qst0056', 'TRUNCATE'),
('qst0057', 'To speed up data retrieval operations'),
('qst0058', 'LEFT JOIN'),
('qst0059', 'NOT NULL'),
('qst0060', 'ALTER TABLE');

-- ---------------------------------------------------------------------
-- 10. Seeding Exams and Exam Questions (1 Exam per Module)
-- ---------------------------------------------------------------------
INSERT INTO exams (exam_id, title, start_time, duration_minutes, total_marks, created_by) VALUES
('exm0001', 'Programming Fundamentals - Term Test', '2026-03-10 10:00:00', 30, 50, 'usr0001'),
('exm0002', 'Object Oriented Programming - Term Test', '2026-03-12 10:00:00', 45, 50, 'usr0001'),
('exm0003', 'Internet Technologies - Term Test', '2026-03-15 10:00:00', 30, 50, 'usr0001'),
('exm0004', 'Standalone Application (JavaFX) - Term Test', '2026-03-18 10:00:00', 45, 50, 'usr0001'),
('exm0005', 'Enterprise Engineering - Term Test', '2026-03-20 10:00:00', 60, 50, 'usr0001'),
('exm0006', 'Database Management Systems - Term Test', '2026-03-22 10:00:00', 45, 50, 'usr0001');

INSERT INTO exam_question (exam_id, question_id) VALUES
('exm0001', 'qst0001'), ('exm0001', 'qst0002'), ('exm0001', 'qst0003'), ('exm0001', 'qst0004'), ('exm0001', 'qst0005'),
('exm0001', 'qst0006'), ('exm0001', 'qst0007'), ('exm0001', 'qst0008'), ('exm0001', 'qst0009'), ('exm0001', 'qst0010'),

('exm0002', 'qst0011'), ('exm0002', 'qst0012'), ('exm0002', 'qst0013'), ('exm0002', 'qst0014'), ('exm0002', 'qst0015'),
('exm0002', 'qst0016'), ('exm0002', 'qst0017'), ('exm0002', 'qst0018'), ('exm0002', 'qst0019'), ('exm0002', 'qst0020'),

('exm0003', 'qst0021'), ('exm0003', 'qst0022'), ('exm0003', 'qst0023'), ('exm0003', 'qst0024'), ('exm0003', 'qst0025'),
('exm0003', 'qst0026'), ('exm0003', 'qst0027'), ('exm0003', 'qst0028'), ('exm0003', 'qst0029'), ('exm0003', 'qst0030'),

('exm0004', 'qst0031'), ('exm0004', 'qst0032'), ('exm0004', 'qst0033'), ('exm0004', 'qst0034'), ('exm0004', 'qst0035'),
('exm0004', 'qst0036'), ('exm0004', 'qst0037'), ('exm0004', 'qst0038'), ('exm0004', 'qst0039'), ('exm0004', 'qst0040'),

('exm0005', 'qst0041'), ('exm0005', 'qst0042'), ('exm0005', 'qst0043'), ('exm0005', 'qst0044'), ('exm0005', 'qst0045'),
('exm0005', 'qst0046'), ('exm0005', 'qst0047'), ('exm0005', 'qst0048'), ('exm0005', 'qst0049'), ('exm0005', 'qst0050'),

('exm0006', 'qst0051'), ('exm0006', 'qst0052'), ('exm0006', 'qst0053'), ('exm0006', 'qst0054'), ('exm0006', 'qst0055'),
('exm0006', 'qst0056'), ('exm0006', 'qst0057'), ('exm0006', 'qst0058'), ('exm0006', 'qst0059'), ('exm0006', 'qst0060');
