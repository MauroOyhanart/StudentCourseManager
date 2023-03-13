# StudentCourseManager
A desktop JavaFX application to manually load, and visualize a course and the relationships between its subjects.

Made with Java 17 and Java FX 8, using Maven Repositories.

Utilizes an embedded database, SQLite.

By performing

mvn clean

mvn install

on a local terminal in the code's folder, the application will produce a shaded jar that has all the libraries bundled in it.

This allows for using packaging tools such as launch4j (https://launch4j.sourceforge.net) to generate an .exe file in Windows that is portable.

All is needed to run such .exe is a JRE 17 and the javapath set. The .exe will generate a sample.db file for storing the data.

This is an example of a course created and visualized using this app.

![image](https://user-images.githubusercontent.com/71988296/223075990-16bfa7ab-f184-4c4c-ad69-0a9d0751fe79.png)

![image](https://user-images.githubusercontent.com/71988296/224799890-36766c20-a49c-4e8d-ac11-4393367a9d5b.png)


In the git folder /output, there is a .jar, a .exe, and the ico.
