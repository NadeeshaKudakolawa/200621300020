import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Officer {
    private String officerId;
    private String name;
    private String department;
    private String grade;
    private int yearsOfService;

    public Officer(String officerId, String name, String department, String grade, int yearsOfService) {
        this.officerId = officerId;
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.yearsOfService = yearsOfService;
    }

    public String getOfficerId() {
        return officerId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getGrade() {
        return grade;
    }

    public int getYearsOfService() {
        return yearsOfService;
    }
}

class TrainingProgramme {

    private String trainingId;
    private String trainingTitle;

    private List<EligibilityRule> eligibilityRules = new ArrayList<>();

    public TrainingProgramme(String trainingId, String trainingTitle) {
        this.trainingId = trainingId;
        this.trainingTitle = trainingTitle;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public String getTrainingTitle() {
        return trainingTitle;
    }

    public void addEligibilityRule(EligibilityRule rule) {
        eligibilityRules.add(rule);
    }

    public boolean isEligible(Officer officer, List<TrainingHistory> history) {
        for (EligibilityRule rule : eligibilityRules) {
            if (!rule.isEligible(officer, this, history)) {
                return false;
            }
        }
        return true;
    }

    public String getEligibilityFailureReason(Officer officer, List<TrainingHistory> history) {
        for (EligibilityRule rule : eligibilityRules) {
            if (!rule.isEligible(officer, this, history)) {
                return rule.getFailureReason();
            }
        }
        return "Officer is eligible.";
    }
}

class TrainingHistory {

    private String officerId;
    private String trainingId;
    private LocalDate participationDate;

    public TrainingHistory(String officerId, String trainingId, LocalDate participationDate) {
        this.officerId = officerId;
        this.trainingId = trainingId;
        this.participationDate = participationDate;
    }

    public String getOfficerId() {
        return officerId;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public LocalDate getParticipationDate() {
        return participationDate;
    }
}

interface EligibilityRule {
    boolean isEligible(Officer officer, TrainingProgramme programme, List<TrainingHistory> history);
    String getFailureReason();
}

class DepartmentRule implements EligibilityRule {
    private Set<String> allowedDepartments;

    public DepartmentRule(Set<String> allowedDepartments) {
        this.allowedDepartments = allowedDepartments;
    }

    @Override
    public boolean isEligible(Officer officer, TrainingProgramme programme, List<TrainingHistory> history) {
        return allowedDepartments.contains(officer.getDepartment());
    }

    @Override
    public String getFailureReason() {
        return "Officer's department is not eligible " + "for this training programme.";
    }
}

class GradeRule implements EligibilityRule {
    private Set<String> allowedGrades;

    public GradeRule(Set<String> allowedGrades) {
        this.allowedGrades = allowedGrades;
    }

    @Override
    public boolean isEligible(Officer officer, TrainingProgramme programme, List<TrainingHistory> history) {
        return allowedGrades.contains(officer.getGrade());
    }

    @Override
    public String getFailureReason() {
        return "Officer's grade is not eligible " + "for this training programme.";
    }
}

class ServiceYearsRule implements EligibilityRule {
    private int minimumYears;

    public ServiceYearsRule(int minimumYears) {
        this.minimumYears = minimumYears;
    }

    @Override
    public boolean isEligible(Officer officer, TrainingProgramme programme, List<TrainingHistory> history) {
        return officer.getYearsOfService() >= minimumYears;
    }

    @Override
    public String getFailureReason() {
        return "Officer does not have the required " + "number of years of service.";
    }
}

class Previous12MonthsRule implements EligibilityRule {

    @Override
    public boolean isEligible(Officer officer, TrainingProgramme programme, List<TrainingHistory> history) {

        LocalDate today = LocalDate.now();

        LocalDate twelveMonthsAgo = today.minusMonths(12);

        for (TrainingHistory record : history) {
            boolean sameOfficer = record.getOfficerId().equals(officer.getOfficerId());

            boolean sameTraining = record.getTrainingId().equals(programme.getTrainingId());

            boolean withinLast12Months = !record.getParticipationDate().isBefore(twelveMonthsAgo);

            if (sameOfficer && sameTraining && withinLast12Months) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getFailureReason() {
        return "Officer has participated in the same " + "training programme within the previous " + "12 months.";
    }
}

class EligibilityManager {
    public boolean checkEligibility(Officer officer, TrainingProgramme programme, List<TrainingHistory> history) {
        return programme.isEligible(officer, history);
    }

    public void displayEligibilityResult(Officer officer, TrainingProgramme programme, List<TrainingHistory> history) {
        System.out.println("\n");
        System.out.println("Officer: " + officer.getName());
        System.out.println("Training: " + programme.getTrainingTitle());

        if (checkEligibility(officer, programme, history)) {
            System.out.println("Eligibility: APPROVED");
        } else {
            System.out.println("Eligibility: REJECTED");
            System.out.println("Reason: " + programme.getEligibilityFailureReason(officer, history));
        }
    }
}

public class Main {
    public static void main(String[] args) {

        Officer financeOfficer = new Officer("OFF001", "A. Perera", "Finance", "Grade III", 5);

        Officer itOfficer = new Officer("OFF002", "B. Silva", "IT", "Grade II", 6);

        Officer newOfficer = new Officer("OFF003", "C. Fernando", "Finance", "Grade III", 1);

        List<TrainingHistory> history = new ArrayList<>();

        history.add(new TrainingHistory("OFF001", "TR001", LocalDate.now().minusMonths(6)));

        TrainingProgramme financialProgramme = new TrainingProgramme("TR001", "Financial Management Programme");

        financialProgramme.addEligibilityRule(new DepartmentRule(Set.of("Finance", "Budget", "Planning")));

        financialProgramme.addEligibilityRule(new Previous12MonthsRule());

        TrainingProgramme managementProgramme = new TrainingProgramme("TR002", "Management Development Programme");

        managementProgramme.addEligibilityRule(new GradeRule(Set.of("Grade II", "Grade I")));

        managementProgramme.addEligibilityRule(new ServiceYearsRule(3));

        EligibilityManager manager = new EligibilityManager();

        manager.displayEligibilityResult(financeOfficer, financialProgramme, history);

        manager.displayEligibilityResult(itOfficer, managementProgramme, history);

        manager.displayEligibilityResult(newOfficer, managementProgramme, history);}}