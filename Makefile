JAVAC=javac
JVM = java

SRC_PATH = src/
BIN_PATH = bin/

MAIN = Shell_Project
NAME_JAR = Shell_Project

all:
	$(JAVAC) $(SRC_PATH)*.java -d $(BIN_PATH)

clean:
	rm -f $(BIN_PATH)*.class

run:
	$(JVM) -classpath $(BIN_PATH) $(MAIN)

runjar:
	$(JVM) -jar build/dist/$(NAME_JAR).jar

jar: all
	jar cvmf META-INF/MANIFEST.MF build/dist/$(NAME_JAR).jar -C $(BIN_PATH) .

tar: clean
	tar cf $(NAME_JAR).tar Makefile README.txt META-INF/ bin/ src/ build/lib/ javadoc/ build/dist/$(NAME_JAR).jar build/build.xml build/icon.icns build/info.plist

doc:
	javadoc -encoding utf8 -docencoding utf8 -charset utf8 -d javadoc -author $(SRC_PATH)*.java

macos: jar
	ant bundle -f build/build.xml

