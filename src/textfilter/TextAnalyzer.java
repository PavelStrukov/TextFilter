/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textfilter;

/**
 *
 * @author Dell
 */
public interface TextAnalyzer {
    Label processText(String text);
    
    enum Label {
    SPAM, NEGATIVE_TEXT, TOO_LONG, OK
}
}
