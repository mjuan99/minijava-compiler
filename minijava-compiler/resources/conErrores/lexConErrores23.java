///[Error:'\u12'|10]

private boolean isHexadecimalDigit(char character){
        return Character.isDigit(character) || (character >= 'a' && character <= 'f') || (character >= 'A' && character <= 'F');
        }

private void initializeKeywordsMap(){
        keywordsMap = new HashMap<>();
        keywordsMap.put("class", "pr_class");
        '\u12'
        keywordsMap.put("interface", "pr_interface");
