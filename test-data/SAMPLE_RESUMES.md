# Sample Resumes for Testing

This directory contains sample resume files for testing the Resume Parser and Job Matcher application.

## Creating Test Resumes

To test the application, create PDF or DOCX files with the following structure:

### Example Resume Format

```
John Doe
john.doe@email.com | (555) 123-4567 | LinkedIn: linkedin.com/in/johndoe

PROFESSIONAL SUMMARY
Experienced software engineer with 5+ years of expertise in full-stack development.

SKILLS
Java, Spring Boot, Python, MySQL, PostgreSQL, REST APIs, Git, Docker, Kubernetes,
JavaScript, React, Node.js, Microservices, Agile, CI/CD

EDUCATION
Bachelor of Science in Computer Science
Stanford University, California
Graduated: May 2018
GPA: 3.8/4.0

WORK EXPERIENCE

Senior Software Engineer | Tech Corp Inc | June 2020 - Present
- Designed and implemented microservices architecture using Spring Boot and Docker
- Led a team of 5 developers in building RESTful APIs serving 1M+ requests daily
- Improved system performance by 40% through database optimization
- Mentored junior developers and conducted code reviews

Software Engineer | StartUp Solutions | June 2018 - May 2020
- Developed full-stack web applications using React and Node.js
- Implemented CI/CD pipelines using Jenkins and GitLab
- Collaborated with cross-functional teams in Agile environment
- Built automated testing suites increasing code coverage to 85%

CERTIFICATIONS
- AWS Certified Solutions Architect
- Oracle Certified Professional, Java SE 11 Developer

PROJECTS
E-commerce Platform: Built scalable microservices-based platform handling 10K concurrent users
Data Analytics Dashboard: Created real-time analytics dashboard using React and Python
```

### Example Resume 2 - Junior Developer

```
Jane Smith
jane.smith@email.com | (555) 987-6543

OBJECTIVE
Recent computer science graduate seeking entry-level software development position.

SKILLS
Python, Java, JavaScript, HTML, CSS, SQL, Git, Linux, Object-Oriented Programming

EDUCATION
Bachelor of Science in Computer Science
University of California, Berkeley
Graduated: May 2023
GPA: 3.6/4.0

Relevant Coursework:
- Data Structures and Algorithms
- Database Systems
- Web Development
- Software Engineering

INTERNSHIP EXPERIENCE

Software Engineering Intern | Google | Summer 2022
- Developed features for internal tools using Python and JavaScript
- Collaborated with team members using Git and Agile methodologies
- Participated in code reviews and testing processes

ACADEMIC PROJECTS

Social Media Platform
- Built a full-stack social media application using React, Node.js, and MongoDB
- Implemented user authentication and real-time chat features
- Deployed on AWS using Docker containers

Machine Learning Image Classifier
- Developed image classification model using TensorFlow and Python
- Achieved 92% accuracy on test dataset
- Created REST API for model deployment

TECHNICAL SKILLS
Programming Languages: Python, Java, JavaScript, SQL, HTML/CSS
Frameworks: React, Node.js, Express, Flask
Tools: Git, Docker, VS Code, PyCharm
Databases: MySQL, MongoDB, PostgreSQL
```

## Sample Job Descriptions

### Job Description 1 - Senior Java Developer

```
Job Title: Senior Java Developer

Required Skills:
Java, Spring Boot, Microservices, REST APIs, MySQL, Docker, Kubernetes, Git, Agile

Experience: 5+ years

Responsibilities:
- Design and develop scalable microservices using Spring Boot
- Write clean, maintainable, and well-documented code
- Collaborate with cross-functional teams
- Mentor junior developers
- Participate in code reviews and architecture discussions

Qualifications:
- Bachelor's degree in Computer Science or related field
- Strong understanding of object-oriented programming
- Experience with cloud platforms (AWS, Azure, or GCP)
- Excellent problem-solving skills
```

### Job Description 2 - Junior Python Developer

```
Job Title: Junior Python Developer

Required Skills:
Python, SQL, Git, REST APIs, Linux, Object-Oriented Programming

Experience: 0-2 years (Entry Level)

Responsibilities:
- Develop and maintain Python applications
- Write unit tests and documentation
- Collaborate with senior developers
- Learn and apply best practices
- Participate in Agile ceremonies

Qualifications:
- Bachelor's degree in Computer Science or related field
- Knowledge of Python and web frameworks (Flask or Django)
- Understanding of database concepts
- Strong willingness to learn
- Good communication skills
```

## Testing Workflow

1. **Create Resume Files**: Convert the above examples to PDF or DOCX format
2. **Load into Application**: Use the "Load Resume(s)" button
3. **Test Matching**: Paste job descriptions and click "Match Candidates"
4. **Verify Results**: Check that:
   - John Doe scores high for Senior Java Developer role
   - Jane Smith scores high for Junior Python Developer role
   - Match scores reflect skill overlap accurately
   - Missing skills are correctly identified

## Tips for Creating Test Resumes

- Include clear section headers (SKILLS, EDUCATION, EXPERIENCE)
- List skills with commas or bullets
- Use standard date formats for education/experience
- Include contact information at the top
- Use common section names recognized by the parser

## Expected Results

When matching John Doe against Senior Java Developer:

- **Expected Score**: 70-85%
- **Matched Skills**: Java, Spring Boot, Microservices, REST APIs, MySQL, Docker, Kubernetes, Git
- **Missing Skills**: None or very few

When matching Jane Smith against Junior Python Developer:

- **Expected Score**: 75-90%
- **Matched Skills**: Python, SQL, Git, REST APIs, Linux, OOP
- **Missing Skills**: Minimal to none

When matching Jane Smith against Senior Java Developer:

- **Expected Score**: 30-50%
- **Matched Skills**: Java, Git
- **Missing Skills**: Spring Boot, Microservices, Docker, Kubernetes, MySQL
