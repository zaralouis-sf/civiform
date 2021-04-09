package services.applicant.question;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import services.Path;
import services.applicant.ValidationErrorMessage;
import services.question.exceptions.TranslationNotFoundException;
import services.question.types.MultiOptionQuestionDefinition;

// TODO(https://github.com/seattle-uat/civiform/issues/396): Implement a question that allows for
// multiple answer selections (i.e. the value is a list)
public class SingleSelectQuestion implements PresentsErrors {

  private final ApplicantQuestion applicantQuestion;
  private Optional<String> selectedOptionValue;

  public SingleSelectQuestion(ApplicantQuestion applicantQuestion) {
    this.applicantQuestion = applicantQuestion;
    assertQuestionType();
  }

  @Override
  public boolean hasQuestionErrors() {
    return !getQuestionErrors().isEmpty();
  }

  public ImmutableSet<ValidationErrorMessage> getQuestionErrors() {
    // TODO(https://github.com/seattle-uat/civiform/issues/416): Implement validation
    return ImmutableSet.of();
  }

  @Override
  public boolean hasTypeSpecificErrors() {
    // There are no inherent requirements in a multi-option question.
    return false;
  }

  public boolean hasValue() {
    return getSelectedOptionValue().isPresent();
  }

  public Optional<String> getSelectedOptionValue() {
    if (selectedOptionValue != null) {
      return selectedOptionValue;
    }

    selectedOptionValue = applicantQuestion.getApplicantData().readString(getSelectionPath());

    return selectedOptionValue;
  }

  public void assertQuestionType() {
    if (!applicantQuestion.getType().isMultiOptionType()) {
      throw new RuntimeException(
          String.format(
              "Question is not a multi-option question: %s (type: %s)",
              applicantQuestion.getQuestionDefinition().getPath(),
              applicantQuestion.getQuestionDefinition().getQuestionType()));
    }
  }

  public MultiOptionQuestionDefinition getQuestionDefinition() {
    assertQuestionType();
    return (MultiOptionQuestionDefinition) applicantQuestion.getQuestionDefinition();
  }

  public Path getSelectionPath() {
    return getQuestionDefinition().getSelectionPath();
  }

  public ImmutableList<String> getOptions() {
    try {
      return getQuestionDefinition()
          .getOptionsForLocale(applicantQuestion.getApplicantData().preferredLocale());
    } catch (TranslationNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}