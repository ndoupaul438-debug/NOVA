package com.nova.app.school;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NovaSchoolLearning {

    private static final String PREFS = "nova_school_learning";
    private static final String COMPLETED = "completed_lessons";
    private static final String SCORES = "quiz_scores";

    private NovaSchoolLearning() {}

    public static List<Lesson> lessons(String course) {
        List<Lesson> result = new ArrayList<>();

        if (course == null) {
            return result;
        }


        switch (course) {

            case "Termux Fundamentals":
                add(result, "termux-01", course,
                        "Learn what Termux is and how it provides a Linux terminal environment on Android.",
                        "Termux combines a terminal interface with package-managed Linux utilities. Learn the shell, commands, paths, packages and safe project organization.");
                break;

            case "Linux Commands":
                add(result, "linux-01", course,
                        "Learn essential Linux commands for navigation, files, processes and system information.",
                        "Practice commands such as pwd, ls, cd, mkdir, cp, mv, rm, cat, grep and find. Always verify paths before deleting files.");
                break;

            case "Files & Storage":
                add(result, "storage-01", course,
                        "Understand Android storage, Termux home directories and safe file management.",
                        "Learn how files and folders are organized, how to inspect permissions and how to work safely with shared Android storage.");
                break;

            case "Bash & Shell Scripting":
                add(result, "bash-01", course,
                        "Learn how Bash scripts automate repeated terminal tasks.",
                        "Study variables, arguments, conditions, loops, functions and exit codes. Build small scripts that automate safe local tasks.");
                break;

            case "Python in Termux":
                add(result, "python-termux-01", course,
                        "Learn how to write and execute Python programs inside Termux.",
                        "Start with variables, input, conditions, loops, functions, files and modules. Build useful command-line programs.");
                break;

            case "Java Development":
                add(result, "java-01", course,
                        "Learn Java development using the Android phone and Termux.",
                        "Study classes, objects, methods, collections, exceptions and compilation. Build small Java console applications.");
                break;

            case "Git & GitHub":
                add(result, "git-01", course,
                        "Learn version control and collaborative development with Git and GitHub.",
                        "Practice repositories, commits, branches, merging, remotes and pull requests. Learn why small meaningful commits make projects easier to manage.");
                break;

            case "Web Development":
                add(result, "web-01", course,
                        "Learn the foundations of modern web development.",
                        "HTML provides structure, CSS controls presentation and JavaScript provides behaviour. Combine them to build interactive web pages.");
                break;

            case "Local Web Servers":
                add(result, "server-01", course,
                        "Learn how to run websites and development servers locally.",
                        "Understand localhost, ports, static files and development servers. Test your own projects locally without exposing them to unauthorized networks.");
                break;

            case "Package Management":
                add(result, "packages-01", course,
                        "Learn how package managers install and maintain development tools.",
                        "Understand repositories, package installation, upgrades, removal and dependency management. Keep development environments organized.");
                break;

            case "Android Development":
                add(result, "android-01", course,
                        "Learn the foundations of native Android application development.",
                        "Study Android projects, activities, layouts, resources, manifests, permissions and application lifecycle concepts.");
                break;

            case "Automation":
                add(result, "automation-01", course,
                        "Learn how scripts and tools can automate repetitive development tasks.",
                        "Design safe automation workflows for file processing, builds, testing and project maintenance. Always validate commands before executing destructive operations.");
                break;

            case "Networking Fundamentals":
                add(result, "networking-01", course,
                        "Learn fundamental networking concepts and terminology.",
                        "Study IP addresses, ports, protocols, DNS, DHCP, routers and local networks. Use only networks and devices you are authorized to test.");
                break;

            case "Cybersecurity Labs":
                add(result, "cyber-labs-01", course,
                        "Learn cybersecurity through authorized and isolated practice laboratories.",
                        "Study defensive security, authentication, vulnerabilities, logs and incident investigation using intentionally vulnerable lab systems and CTF environments.");
                break;

            case "Termux Challenges":
                add(result, "termux-challenges-01", course,
                        "Test your Termux and Linux skills through practical challenges.",
                        "Complete progressively harder tasks involving files, commands, scripting, Git, Python, networking fundamentals and troubleshooting.");
                break;


            case "Programming Fundamentals":
                add(result, "programming-fundamentals-01", course,
                        "Learn the core ideas used in programming.",
                        "Study variables, data types, operators, input, output, conditions, loops and functions. Practice solving small problems step by step.");
                break;

            case "Object-Oriented Programming":
                add(result, "oop-01", course,
                        "Learn how object-oriented programming organizes software.",
                        "Study classes, objects, constructors, encapsulation, inheritance, polymorphism and interfaces. Apply the ideas to small projects.");
                break;

            case "Data Structures":
                add(result, "data-structures-01", course,
                        "Learn how data can be organized for efficient programs.",
                        "Study arrays, lists, stacks, queues, sets, maps, trees and graphs. Compare structures based on the operations your program needs.");
                break;

            case "Algorithms":
                add(result, "algorithms-01", course,
                        "Learn systematic methods for solving computational problems.",
                        "Study searching, sorting, recursion, iteration and algorithm complexity. Learn to compare solutions using time and space requirements.");
                break;

            case "Debugging":
                add(result, "debugging-01", course,
                        "Learn how to find and fix programming errors.",
                        "Study syntax errors, runtime errors and logic errors. Use logs, breakpoints, test cases and careful isolation to identify problems.");
                break;

            case "Software Engineering":
                add(result, "software-engineering-01", course,
                        "Learn professional approaches to building maintainable software.",
                        "Study requirements, architecture, modular design, documentation, version control, testing, code review and release planning.");
                break;

            case "Code Testing":
                add(result, "code-testing-01", course,
                        "Learn how testing improves software reliability.",
                        "Study unit tests, integration tests, system tests, test cases, assertions and regression testing. Design tests that cover normal and unusual inputs.");
                break;

            case "Code Quality":
                add(result, "code-quality-01", course,
                        "Learn techniques for writing clean and maintainable code.",
                        "Study naming, formatting, functions, modularity, duplication, comments and refactoring. Good code should be understandable as well as functional.");
                break;

            case "Version Control":
                add(result, "version-control-01", course,
                        "Learn how developers manage changes to software projects.",
                        "Study commits, branches, merges, tags and history. Learn how version control makes experimentation and collaboration safer.");
                break;

            case "Build Systems":
                add(result, "build-systems-01", course,
                        "Learn how software projects are compiled, packaged and tested.",
                        "Study source files, dependencies, build configurations, debug builds, release builds and automated build pipelines.");
                break;

            case "Mobile Programming":
                add(result, "mobile-programming-01", course,
                        "Learn programming concepts specific to mobile applications.",
                        "Study mobile interfaces, lifecycle events, local storage, permissions, responsive layouts and efficient use of device resources.");
                break;

            case "Web Programming":
                add(result, "web-programming-01", course,
                        "Learn how programming concepts are applied to interactive websites.",
                        "Study DOM manipulation, events, forms, asynchronous operations, browser storage and communication with web services.");
                break;

            case "API Programming":
                add(result, "api-programming-01", course,
                        "Learn how applications communicate through APIs.",
                        "Study HTTP requests, endpoints, methods, status codes, JSON, authentication concepts, validation and error handling.");
                break;

            case "Project Development":
                add(result, "project-development-01", course,
                        "Learn how to turn a programming idea into a complete project.",
                        "Plan requirements, design components, implement features, test the project, document it and prepare a reliable build or release.");
                break;


            case "Java":
                add(result, "java-school-01", course,
                        "Learn Java programming from fundamentals to practical application.",
                        "Study variables, methods, classes, objects, collections, exceptions and file handling. Build progressively larger Java programs.");
                break;

            case "Kotlin":
                add(result, "kotlin-01", course,
                        "Learn Kotlin, a modern language widely used for Android development.",
                        "Study variables, functions, null safety, classes, collections, lambdas and Android-oriented Kotlin patterns.");
                break;

            case "Python":
                add(result, "python-01", course,
                        "Learn Python for programming, automation and data work.",
                        "Study variables, collections, conditions, loops, functions, modules, files and exceptions before progressing to practical projects.");
                break;

            case "JavaScript":
                add(result, "javascript-01", course,
                        "Learn JavaScript for interactive applications and websites.",
                        "Study variables, functions, objects, arrays, events, DOM manipulation, asynchronous programming and APIs.");
                break;

            case "TypeScript":
                add(result, "typescript-01", course,
                        "Learn TypeScript and type-safe JavaScript development.",
                        "Study types, interfaces, functions, generics, classes and compiler checks. Learn how types can prevent common programming errors.");
                break;

            case "C":
                add(result, "c-01", course,
                        "Learn the foundations of the C programming language.",
                        "Study variables, pointers, arrays, functions, structures, memory and compilation. Practice with small command-line programs.");
                break;

            case "C++":
                add(result, "cpp-01", course,
                        "Learn C++ programming and its powerful abstraction features.",
                        "Study variables, functions, classes, templates, STL containers, memory management and object-oriented programming.");
                break;

            case "C#":
                add(result, "csharp-01", course,
                        "Learn C# for application and game-oriented programming.",
                        "Study types, classes, interfaces, collections, exceptions and asynchronous programming. Apply the language to practical software projects.");
                break;

            case "PHP":
                add(result, "php-01", course,
                        "Learn PHP for server-side web development.",
                        "Study variables, arrays, functions, forms, sessions, database access, validation and secure server-side programming.");
                break;

            case "SQL":
                add(result, "sql-01", course,
                        "Learn how to create, query and manage relational data.",
                        "Study tables, keys, SELECT, INSERT, UPDATE, DELETE, JOIN, grouping, constraints and database design.");
                break;

            case "Bash":
                add(result, "bash-school-01", course,
                        "Learn Bash programming for Linux automation.",
                        "Study variables, arguments, conditions, loops, functions, pipes, redirection and command composition.");
                break;

            case "Programming Challenges":
                add(result, "programming-challenges-01", course,
                        "Develop problem-solving skills through programming challenges.",
                        "Solve progressively harder problems involving strings, arrays, mathematics, searching, sorting, recursion and data structures.");
                break;

            case "Competitive Programming":
                add(result, "competitive-programming-01", course,
                        "Develop speed, accuracy and algorithmic thinking for programming competitions.",
                        "Practice complexity analysis, greedy methods, dynamic programming, graphs, number theory and timed problem solving.");
                break;


            case "Computer Fundamentals":
                add(result, "computer-fundamentals-01", course,
                        "Learn the basic concepts of computers and digital systems.",
                        "Study input, processing, output, storage, software, hardware, operating systems and basic computer terminology.");
                break;

            case "Operating Systems":
                add(result, "operating-systems-01", course,
                        "Learn how operating systems manage computer resources.",
                        "Study processes, memory, storage, files, permissions, users, applications and the role of the operating system.");
                break;

            case "Computer Hardware":
                add(result, "computer-hardware-01", course,
                        "Learn the main components inside computers and mobile devices.",
                        "Study CPUs, RAM, storage, motherboards, power systems, displays, peripherals and basic hardware diagnostics.");
                break;

            case "Networking":
                add(result, "networking-01", course,
                        "Learn how computers and devices communicate across networks.",
                        "Study network topologies, IP addresses, ports, switches, routers, DNS, DHCP and basic troubleshooting.");
                break;

            case "Internet Technologies":
                add(result, "internet-technologies-01", course,
                        "Learn the technologies that make the Internet work.",
                        "Study clients, servers, HTTP, HTTPS, DNS, URLs, browsers, web hosting, domains and basic Internet architecture.");
                break;

            case "Cloud Computing":
                add(result, "cloud-computing-01", course,
                        "Learn the fundamentals of cloud computing.",
                        "Study cloud services, virtual machines, storage, databases, networking, scalability and the differences between local and cloud infrastructure.");
                break;

            case "Cybersecurity":
                add(result, "cybersecurity-01", course,
                        "Learn the foundations of defensive cybersecurity.",
                        "Study authentication, access control, encryption, vulnerabilities, secure development, monitoring and incident response using authorized environments.");
                break;

            case "Databases":
                add(result, "databases-01", course,
                        "Learn how databases store and retrieve structured information.",
                        "Study tables, records, fields, keys, relationships, SQL queries, indexes, normalization and database security.");
                break;

            case "Programming":
                add(result, "ict-programming-01", course,
                        "Understand how programming supports ICT systems.",
                        "Study algorithms, variables, control flow, functions, data structures and software development through practical examples.");
                break;

            case "Artificial Intelligence":
                add(result, "artificial-intelligence-01", course,
                        "Learn the foundations of artificial intelligence.",
                        "Study data, machine learning, models, training, evaluation, inference, neural networks and responsible AI development.");
                break;

            case "Data Science":
                add(result, "data-science-01", course,
                        "Learn how data can be collected, cleaned, analyzed and visualized.",
                        "Study datasets, variables, data cleaning, descriptive statistics, visualization, interpretation and introductory machine learning.");
                break;

            case "IoT":
                add(result, "iot-01", course,
                        "Learn how connected devices collect and exchange information.",
                        "Study sensors, actuators, microcontrollers, communication protocols, cloud connections and IoT security.");
                break;

            case "Robotics":
                add(result, "robotics-01", course,
                        "Learn the foundations of robotics and automated systems.",
                        "Study sensors, actuators, controllers, motors, robot logic, navigation and the interaction between hardware and software.");
                break;

            case "Digital Electronics":
                add(result, "digital-electronics-01", course,
                        "Learn how digital electronic systems represent and process information.",
                        "Study binary numbers, logic gates, truth tables, circuits, flip-flops, counters and basic digital system design.");
                break;

            case "Mobile Technology":
                add(result, "mobile-technology-01", course,
                        "Learn how modern mobile devices and applications work.",
                        "Study mobile hardware, operating systems, wireless communication, sensors, applications, permissions, mobile security and power management.");
                break;


            default:
                add(result,
                        "general-01",
                        course,
                        "Introduction to " + course,
                        "CIA AI will guide you through the fundamentals of this subject and provide practical activities.");
                break;
        }

        addProgressionTopics(result, course);

        return result;
    }

    private static void add(
            List<Lesson> result,
            String id,
            String title,
            String description,
            String content
    ) {
        result.add(new Lesson(id, title, description, content));
    }


    /*
     * MULTI-TOPIC COURSE PROGRESSION
     *
     * The original lesson for every course remains Topic 1.
     * These additional topics create a structured learning path.
     */
    private static void addProgressionTopics(
            List<Lesson> result,
            String course
    ) {
        switch (course) {

            case "Termux Fundamentals":
                add(result, "termux-02", course + " — Topic 2",
                        "Navigate the Termux filesystem.",
                        "Learn absolute and relative paths, home directories, working directories and safe navigation.");
                add(result, "termux-03", course + " — Topic 3",
                        "Manage files and directories.",
                        "Create, copy, move, rename and inspect files safely.");
                add(result, "termux-04", course + " — Topic 4",
                        "Install development tools.",
                        "Learn package repositories, package search, installation, upgrades and dependency management.");
                add(result, "termux-05", course + " — Topic 5",
                        "Build a Termux project.",
                        "Combine terminal commands into a small practical project and document the workflow.");
                break;

            case "Linux Commands":
                add(result, "linux-02", course + " — Topic 2",
                        "Filesystem navigation.",
                        "Use paths, directories and file listings to move efficiently through Linux.");
                add(result, "linux-03", course + " — Topic 3",
                        "File operations.",
                        "Practice creating, copying, moving, renaming and safely removing files.");
                add(result, "linux-04", course + " — Topic 4",
                        "Processes and system information.",
                        "Inspect running processes, resource information and system details.");
                add(result, "linux-05", course + " — Topic 5",
                        "Command-line problem solving.",
                        "Combine commands, pipes and filters to solve practical Linux tasks.");
                break;

            case "Files & Storage":
                add(result, "storage-02", course + " — Topic 2",
                        "Directories and paths.",
                        "Understand absolute paths, relative paths and directory organization.");
                add(result, "storage-03", course + " — Topic 3",
                        "File permissions.",
                        "Learn permissions, ownership concepts and safe access management.");
                add(result, "storage-04", course + " — Topic 4",
                        "Android shared storage.",
                        "Understand Termux shared storage and safe interaction with Android files.");
                add(result, "storage-05", course + " — Topic 5",
                        "Storage project.",
                        "Design an organized project directory and safely manage its files.");
                break;

            case "Bash & Shell Scripting":
                add(result, "bash-02", course + " — Topic 2",
                        "Variables and input.",
                        "Create variables, accept user input and display useful results.");
                add(result, "bash-03", course + " — Topic 3",
                        "Conditions and loops.",
                        "Use if statements, case statements and loops to control program flow.");
                add(result, "bash-04", course + " — Topic 4",
                        "Functions and arguments.",
                        "Create reusable functions and scripts that accept command-line arguments.");
                add(result, "bash-05", course + " — Topic 5",
                        "Automation project.",
                        "Build a safe Bash automation tool for a repeated local task.");
                break;

            case "Python in Termux":
                add(result, "python-termux-02", course + " — Topic 2",
                        "Python data and control flow.",
                        "Work with strings, lists, dictionaries, conditions and loops.");
                add(result, "python-termux-03", course + " — Topic 3",
                        "Functions and modules.",
                        "Organize Python programs into reusable functions and modules.");
                add(result, "python-termux-04", course + " — Topic 4",
                        "Files and exceptions.",
                        "Read and write files and handle expected errors safely.");
                add(result, "python-termux-05", course + " — Topic 5",
                        "Build a command-line application.",
                        "Combine Python fundamentals into a useful Termux application.");
                break;

            case "Java Development":
                add(result, "java-02", course + " — Topic 2",
                        "Java classes and objects.",
                        "Create classes, constructors, fields and methods.");
                add(result, "java-03", course + " — Topic 3",
                        "Collections and exceptions.",
                        "Use collections and handle exceptional situations safely.");
                add(result, "java-04", course + " — Topic 4",
                        "Files and application structure.",
                        "Organize Java applications and work with local files.");
                add(result, "java-05", course + " — Topic 5",
                        "Java project.",
                        "Design and build a small complete Java console application.");
                break;

            case "Git & GitHub":
                add(result, "git-02", course + " — Topic 2",
                        "Commits and history.",
                        "Create meaningful commits and inspect project history.");
                add(result, "git-03", course + " — Topic 3",
                        "Branches and merging.",
                        "Create branches, develop features and merge changes safely.");
                add(result, "git-04", course + " — Topic 4",
                        "Remote repositories.",
                        "Connect local projects to GitHub and synchronize changes.");
                add(result, "git-05", course + " — Topic 5",
                        "Collaborative workflow.",
                        "Practice pull requests, reviews and conflict resolution.");
                break;

            case "Web Development":
                add(result, "web-02", course + " — Topic 2",
                        "HTML structure.",
                        "Create semantic pages using headings, sections, links, forms and media.");
                add(result, "web-03", course + " — Topic 3",
                        "CSS styling.",
                        "Use selectors, layouts, responsive design and reusable styles.");
                add(result, "web-04", course + " — Topic 4",
                        "JavaScript interaction.",
                        "Handle events, manipulate the DOM and validate user input.");
                add(result, "web-05", course + " — Topic 5",
                        "Build a responsive website.",
                        "Combine HTML, CSS and JavaScript into a complete project.");
                break;

            case "Android Development":
                add(result, "android-02", course + " — Topic 2",
                        "Android project structure.",
                        "Understand activities, resources, manifests and application components.");
                add(result, "android-03", course + " — Topic 3",
                        "Android user interfaces.",
                        "Build native interfaces using layouts, views and event handling.");
                add(result, "android-04", course + " — Topic 4",
                        "Permissions and device APIs.",
                        "Use Android permissions and device capabilities responsibly.");
                add(result, "android-05", course + " — Topic 5",
                        "Build an Android application.",
                        "Plan, implement, test and package a small native Android app.");
                break;

            case "Programming Fundamentals":
                add(result, "programming-fundamentals-02", course + " — Topic 2",
                        "Variables and data types.",
                        "Choose suitable data types and work with stored values.");
                add(result, "programming-fundamentals-03", course + " — Topic 3",
                        "Conditions and loops.",
                        "Build decisions and repetitions into programs.");
                add(result, "programming-fundamentals-04", course + " — Topic 4",
                        "Functions and modularity.",
                        "Break problems into reusable functions.");
                add(result, "programming-fundamentals-05", course + " — Topic 5",
                        "Problem-solving project.",
                        "Solve a complete programming problem from requirements to testing.");
                break;

            case "Object-Oriented Programming":
                add(result, "oop-02", course + " — Topic 2",
                        "Constructors and encapsulation.",
                        "Design objects with controlled state and clear interfaces.");
                add(result, "oop-03", course + " — Topic 3",
                        "Inheritance and polymorphism.",
                        "Reuse behaviour and design flexible object hierarchies.");
                add(result, "oop-04", course + " — Topic 4",
                        "Interfaces and composition.",
                        "Use interfaces and composition to create maintainable systems.");
                add(result, "oop-05", course + " — Topic 5",
                        "Object-oriented project.",
                        "Design a small application using sound object-oriented principles.");
                break;

            case "Data Structures":
                add(result, "data-structures-02", course + " — Topic 2",
                        "Lists and stacks.",
                        "Compare sequential structures and choose suitable operations.");
                add(result, "data-structures-03", course + " — Topic 3",
                        "Queues, sets and maps.",
                        "Select structures based on ordering, uniqueness and lookup needs.");
                add(result, "data-structures-04", course + " — Topic 4",
                        "Trees and graphs.",
                        "Understand hierarchical and connected data.");
                add(result, "data-structures-05", course + " — Topic 5",
                        "Data structure project.",
                        "Choose and implement structures for a practical software problem.");
                break;

            case "Algorithms":
                add(result, "algorithms-02", course + " — Topic 2",
                        "Searching algorithms.",
                        "Compare linear and more efficient searching strategies.");
                add(result, "algorithms-03", course + " — Topic 3",
                        "Sorting algorithms.",
                        "Understand common sorting strategies and their trade-offs.");
                add(result, "algorithms-04", course + " — Topic 4",
                        "Recursion and complexity.",
                        "Analyze recursive solutions and computational complexity.");
                add(result, "algorithms-05", course + " — Topic 5",
                        "Algorithm challenge.",
                        "Design and test an algorithm for a practical programming problem.");
                break;

            case "Debugging":
                add(result, "debugging-02", course + " — Topic 2",
                        "Reading errors and logs.",
                        "Turn compiler messages, exceptions and logs into useful debugging clues.");
                add(result, "debugging-03", course + " — Topic 3",
                        "Test-driven debugging.",
                        "Use focused test cases to reproduce and isolate failures.");
                add(result, "debugging-04", course + " — Topic 4",
                        "Debugging complex programs.",
                        "Trace problems across multiple functions and components.");
                add(result, "debugging-05", course + " — Topic 5",
                        "Broken-system challenge.",
                        "Diagnose and repair a deliberately broken software system.");
                break;

            case "Software Engineering":
                add(result, "software-engineering-02", course + " — Topic 2",
                        "Requirements and planning.",
                        "Turn user needs into clear requirements and development tasks.");
                add(result, "software-engineering-03", course + " — Topic 3",
                        "Architecture and modularity.",
                        "Design maintainable components and system boundaries.");
                add(result, "software-engineering-04", course + " — Topic 4",
                        "Testing and code review.",
                        "Use reviews and testing to improve software quality.");
                add(result, "software-engineering-05", course + " — Topic 5",
                        "Software project.",
                        "Plan and deliver a small software project using an engineering workflow.");
                break;

            case "Code Testing":
                add(result, "code-testing-02", course + " — Topic 2",
                        "Unit testing.",
                        "Create focused tests for individual functions and components.");
                add(result, "code-testing-03", course + " — Topic 3",
                        "Integration testing.",
                        "Verify that components work correctly together.");
                add(result, "code-testing-04", course + " — Topic 4",
                        "Regression testing.",
                        "Prevent previously fixed defects from returning.");
                add(result, "code-testing-05", course + " — Topic 5",
                        "Testing project.",
                        "Build a meaningful automated test suite for a small application.");
                break;

            case "SQL":
                add(result, "sql-02", course + " — Topic 2",
                        "Tables and relationships.",
                        "Design tables, keys and relationships for structured data.");
                add(result, "sql-03", course + " — Topic 3",
                        "Queries and filtering.",
                        "Use SELECT, WHERE, ORDER BY, GROUP BY and useful query patterns.");
                add(result, "sql-04", course + " — Topic 4",
                        "Joins and aggregation.",
                        "Combine related tables and summarize data.");
                add(result, "sql-05", course + " — Topic 5",
                        "Database project.",
                        "Design a small relational database and write useful queries.");
                break;

            default:
                addGenericProgression(result, course);
                break;
        }
    }

    private static void addGenericProgression(
            List<Lesson> result,
            String course
    ) {
        String key = course.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        add(result, key + "-02", course + " — Topic 2",
                "Build the core skills for " + course + ".",
                "Study the next practical concepts in " + course
                        + " and apply them through guided exercises.");

        add(result, key + "-03", course + " — Topic 3",
                "Apply " + course + " concepts.",
                "Practice intermediate concepts in " + course
                        + " using small examples and problem-solving tasks.");

        add(result, key + "-04", course + " — Topic 4",
                "Develop practical skills in " + course + ".",
                "Complete a practical activity that combines the main skills learned so far.");

        add(result, key + "-05", course + " — Topic 5",
                "Complete a project in " + course + ".",
                "Create, test and document a small project demonstrating your skills in "
                        + course + ".");
    }

    public static void markCompleted(Context context, String lessonId) {
        Set<String> completed = completed(context);
        completed.add(lessonId);

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putStringSet(COMPLETED, completed)
                .apply();
    }

    public static boolean isCompleted(Context context, String lessonId) {
        return completed(context).contains(lessonId);
    }

    public static int completedCount(Context context) {
        return completed(context).size();
    }

    public static void saveQuizScore(
            Context context,
            String quizId,
            int score,
            int total
    ) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(
                        SCORES + "_" + quizId,
                        score + "/" + total
                )
                .apply();
    }

    public static int getQuizPercentage(
            Context context,
            String quizId
    ) {
        String value = context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        ).getString(SCORES + "_" + quizId, "");

        if (value.isEmpty() || !value.contains("/")) {
            return 0;
        }

        try {
            String[] parts = value.split("/");
            int score = Integer.parseInt(parts[0]);
            int total = Integer.parseInt(parts[1]);

            if (total <= 0) {
                return 0;
            }

            return (score * 100) / total;

        } catch (Exception e) {
            return 0;
        }
    }

    private static Set<String> completed(Context context) {
        return new HashSet<>(
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                        .getStringSet(
                                COMPLETED,
                                new HashSet<>()
                        )
        );
    }

    public static final class Lesson {

        private final String id;
        private final String title;
        private final String description;
        private final String content;

        public Lesson(
                String id,
                String title,
                String description,
                String content
        ) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getContent() {
            return content;
        }
    }
}
