package forms;

import services.question.types.CheckboxQuestionDefinition;
import services.question.types.QuestionType;

public class CheckboxQuestionForm extends MultiOptionQuestionForm {

  public CheckboxQuestionForm() {
    super(QuestionType.CHECKBOX);
  }

  public CheckboxQuestionForm(CheckboxQuestionDefinition qd) {
    super(qd);
  }
}