///[Error:'\u12|15]

LexicalAnalyzer lexicalAnalyzer = null;
        try {
        SourceFileManager sourceFileManager = new SourceFileManager(args{0});
        lexicalAnalyzer = new LexicalAnalyzer(sourceFileManager);
        } catch (FileNotFoundException exception) {
        System.out.println("File Not Found");
        System.exit(2);
        } catch (IOException exception){
        System.out.println("IO Error");
        System.exit(3);
        }

'\u12