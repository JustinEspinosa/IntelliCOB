package com.github.justinespinosa.intellicob.psi.node;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class DummyFileBuilder {

    private static final String CRLF = "\r\n";
    private static final String IDENTIFICATION = " identification division.";
    private static final String DATA = " data division.";
    private static final String PROCEDURE = " procedure division.";
    private static final String PROGRAMID = " program-id.";
    private static final String PROGRAMIDNAME = " prg.";
    private static final String WORKING_STORAGE = " working-storage section.";
    private static final String PARAGRAPH = " para. continue.";


    public final static class CodeElement {
        private String text;
        private List<CodeElement> children = new ArrayList<>();

        private CodeElement(String text) {
            this(text, null);
        }

        private CodeElement(String text, CodeElement parent) {
            this.text = text;
            if (parent != null) {
                parent.children.add(this);
            }
        }

        @Override
        public String toString() {
            return text + CRLF + children.stream()
                    .map(CodeElement::toString)
                    .collect(joining(CRLF));
        }
    }


    private boolean hasProgramId = false;
    private boolean hasParagraph = false;

    private CodeElement program = new CodeElement("");
    private CodeElement identification = new CodeElement(IDENTIFICATION, program);
    private CodeElement programId = new CodeElement(PROGRAMID, identification);
    private CodeElement data = new CodeElement(DATA, program);
    private CodeElement workingStorage = new CodeElement(WORKING_STORAGE, data);
    private CodeElement procedure = new CodeElement(PROCEDURE, program);


    public DummyFileBuilder withParagraphName(String name) {
        new CodeElement(" " + name + ". continue.", procedure);
        hasParagraph = true;
        return this;
    }

    private void ensureMinimalProgram() {
        if (!hasProgramId) {
            new CodeElement(PROGRAMIDNAME, programId);
        }
        if (!hasParagraph) {
            new CodeElement(PARAGRAPH, procedure);
        }
    }

    public String build() {
        ensureMinimalProgram();
        return program.toString();
    }
}
