JAVAC=javac
sources = $(wildcard ./Projeto/*.java)
classes = $(sources:.java=.class)

all: program

program: $(classes)

Projeto/%.class: Projeto/%.java
	$(JAVAC) $<

clean:
	rm -f ./Projeto/*.class ./Projeto/*.ctxt
