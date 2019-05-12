/*
Задача со stepik.org

 * Представим, вы делаете систему фильтрации комментариев на каком-то веб-портале, будь то новости, видео-хостинг, а может даже для системы онлайн-обучения :)

Вы хотите фильтровать комментарии по разным критериям, уметь легко добавлять новые фильтры и модифицировать старые.

Допустим, мы будем фильтровать спам, комментарии с негативным содержанием и слишком длинные комментарии.
Спам будем фильтровать по наличию указанных ключевых слов в тексте. 
Негативное содержание будем определять по наличию одного из трех смайликов – :( =( :|
Слишком длинные комментарии будем определять исходя из данного числа – максимальной длины комментария.

Вы решили абстрагировать фильтр в виде следующего интерфейса:
interface TextAnalyzer {
    Label processText(String text);
}
Label – тип-перечисление, которые содержит метки, которыми будем помечать текст:
enum Label {
    SPAM, NEGATIVE_TEXT, TOO_LONG, OK
}
Дальше, вам необходимо реализовать три класса, которые реализуют данный интерфейс: SpamAnalyzer, NegativeTextAnalyzer и TooLongTextAnalyzer.
SpamAnalyzer должен конструироваться от массива строк с ключевыми словами. Объект этого класса должен сохранять в своем состоянии этот массив строк в приватном поле keywords.
NegativeTextAnalyzer должен конструироваться конструктором по-умолчанию.
TooLongTextAnalyzer должен конструироваться от int'а с максимальной допустимой длиной комментария. Объект этого класса должен сохранять в своем состоянии это число в приватном поле maxLength.
Наверняка вы уже заметили, что SpamAnalyzer и NegativeTextAnalyzer во многом похожи – они оба проверяют текст на наличие каких-либо ключевых слов (в случае спама мы получаем их из конструктора, в случае негативного текста мы заранее знаем набор грустных смайликов) и в случае нахождения одного из ключевых слов возвращают  Label (SPAM и NEGATIVE_TEXT соответственно), а если ничего не нашлось – возвращают OK.
Давайте эту логику абстрагируем в абстрактный класс KeywordAnalyzer следующим образом:
Выделим два абстрактных метода getKeywords и getLabel, один из которых будет возвращать набор ключевых слов, а второй метку, которой необходимо пометить положительные срабатывания. Нам незачем показывать эти методы потребителям классов, поэтому оставим доступ к ним только для наследников.
Реализуем processText таким образом, чтобы он зависел только от getKeywords и getLabel.
Сделаем SpamAnalyzer и NegativeTextAnalyzer наследниками KeywordAnalyzer и реализуем абстрактные методы.

Последний штрих – написать метод checkLabels, который будет возвращать метку для комментария по набору анализаторов текста. checkLabels должен возвращать первую не-OK метку в порядке данного набора анализаторов, и OK, если все анализаторы вернули OK.
Используйте, пожалуйста, модификатор доступа по-умолчанию для всех классов.
В итоге, реализуйте классы KeywordAnalyzer, SpamAnalyzer, NegativeTextAnalyzer и TooLongTextAnalyzer и метод checkLabels. TextAnalyzer и Label уже подключены, лишние импорты вам не потребуются.
 */

package textfilter;

import textfilter.TextAnalyzer.Label;

/**
 *
 * @author Dell
 */
public class TextFilter {
    
    public static Label checkLabels(TextAnalyzer[] textAnalyzers, String text){
            for (TextAnalyzer analyzer : textAnalyzers) {
                if((analyzer.processText(text)).equals(Label.OK))
                    continue;
                else return analyzer.processText(text);
            }
            return Label.OK;
        }
     
        
    public static void main(String[] args) {
        TooLongTextAnalyzer tooLongTextAnalyzer = new TooLongTextAnalyzer(5);
    NegativeTextAnalyzer negativeTextAnalyzer = new NegativeTextAnalyzer();
    SpamAnalyzer spamAnalyzer = new SpamAnalyzer(new String[]{"haha", "Maxim"});
    System.out.println(checkLabels(new TextAnalyzer[]{tooLongTextAnalyzer, negativeTextAnalyzer}, "text etxt"));
    System.out.println(checkLabels(new TextAnalyzer[]{tooLongTextAnalyzer, negativeTextAnalyzer}, "text"));
    System.out.println(checkLabels(new TextAnalyzer[]{tooLongTextAnalyzer, negativeTextAnalyzer}, "te:("));
    System.out.println(checkLabels(new TextAnalyzer[]{spamAnalyzer, negativeTextAnalyzer}, "Maximt("));
    }
}
    
abstract class KeyWordAnalyzer implements TextAnalyzer{
        
        abstract protected String[] getKeywords();
        abstract protected Label getLabel();

        @Override
        public Label processText(String text) {
            String[] keywords = getKeywords();
            int i = 0;
            while (i<text.length()){
                StringBuilder sb = new StringBuilder();
                while ((i < text.length()) && (text.charAt(i) != ' ')){
                    sb.append(text.charAt(i));
                    i++;
                }
                String word = sb.toString();
                for (String keyword : keywords) {
                    if (keyword.equals(word)) {
                        return getLabel();
                    }
                }
                i++;
            }
            
            return Label.OK;
        }    
    }
    
    class SpamAnalyzer extends KeyWordAnalyzer{

        private String[] keywords;

        public SpamAnalyzer(String[] keywordStrings) {
            this.keywords = keywordStrings;
        }
        
        @Override
        protected String[] getKeywords() {
            return this.keywords;
        }

        @Override
        protected Label getLabel() {
            return Label.SPAM;
        }
    }
    
    class NegativeTextAnalyzer extends KeyWordAnalyzer{
        
        private String[] smiles= {":(", "=(", ":|"};

        @Override
        protected String[] getKeywords() {
            return this.smiles;
        }

        @Override
        protected Label getLabel() {
            return Label.NEGATIVE_TEXT;
        }        
    }
    
    class TooLongTextAnalyzer implements TextAnalyzer{
        private int maxLength;

        public TooLongTextAnalyzer(int length) {
            this.maxLength = length;
        }

        @Override
        public Label processText(String text) {
            if (text.length() > this.maxLength) {
                return Label.TOO_LONG;
            } else{
                return Label.OK;
            }
        }
        
    }
