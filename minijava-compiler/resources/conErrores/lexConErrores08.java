///[Error:'a|8]

public LexicalAnalyzer(SourceFileManager sourceFileManager) throws IOException {
        this.sourceFileManager = sourceFileManager;
        initializeKeywordsMap();
        updateCurrentChar();
        }
        'a