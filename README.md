# StudentCourseManager
A desktop JavaFX application to see a course and the relationships between its subjects.

Made with Java 17 and Java FX 8, using Maven Repositories.

Utilizes an embedded database, SQLite.

By performing

mvn clean

mvn install

on a local terminal in the code's folder, the application will produce a shaded jar that has all the libraries bundled in it.

This allows for using packaging tools such as launch4j (https://launch4j.sourceforge.net) to generate an .exe file in Windows that is portable.

All is needed to run such .exe is a JRE 17 and the javapath set. The .exe will generate a sample.db file for storing the data.

This is an example of a course created and visualized using this app.

![image](https://user-images.githubusercontent.com/71988296/223075814-c760b355-6c30-4757-b1d5-c82ea26d9930.png)



In the git folder /output, there is a .jar, a .exe, and the ico.
