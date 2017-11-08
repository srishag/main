package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a task's deadline in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDeadline(String)}
 */
public class Deadline {

    public static final String MESSAGE_DEADLINE_CONSTRAINTS = "Event must have a valid date input\n"
                    + "Format: DD/MM/YYYY";

    public final String value;
    public final LocalDate localDeadline;
    private String countDown;

    /**
     * Validates given eventDate.
     *
     * @throws IllegalValueException if given eventDate string is invalid.
     */
    public Deadline(String deadline) throws IllegalValueException {
        requireNonNull(deadline);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            localDeadline = LocalDate.parse(deadline, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(MESSAGE_DEADLINE_CONSTRAINTS, e);
        }
        getCountDown();
        this.value = deadline + "\n" + countDown;
    }

    private void getCountDown() {
        ZoneId SGT = ZoneId.of("GMT+8");
        LocalDate currentDate = LocalDate.now(SGT);
        Period period = currentDate.until(localDeadline);

        int years = getYearsUntilDeadline();
        int months = getMonthsUntilDeadline();
        int days = getDaysUntilDeadline();

        if (period.isNegative()) {
            this.countDown = "Event is overdue.";
        } else if (period.isZero()) {
            this.countDown = "Event is today!";
        } else {
            this.countDown = "Event in: " + years + "years " + months + "months " + days + "days";
        }
    }

    public int getYearsUntilDeadline() {
        ZoneId SGT = ZoneId.of("GMT+8");
        LocalDate currentDate = LocalDate.now(SGT);
        Period period = currentDate.until(localDeadline);

        return period.getYears();
    }

    public int getMonthsUntilDeadline() {
        ZoneId SGT = ZoneId.of("GMT+8");
        LocalDate currentDate = LocalDate.now(SGT);
        Period period = currentDate.until(localDeadline);

        return period.getMonths();
    }

    public int getDaysUntilDeadline() {
        ZoneId SGT = ZoneId.of("GMT+8");
        LocalDate currentDate = LocalDate.now(SGT);
        Period period = currentDate.until(localDeadline);

        return period.getDays();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && this.value.equals(((Deadline) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
