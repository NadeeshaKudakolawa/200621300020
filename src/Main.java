import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Officer {
    private String officerId;
    private String name;
    private String department;

    public Officer(String officerId, String name, String department) {
        this.officerId = officerId;
        this.name = name;
        this.department = department;
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
}


class TrainingProgramme {
    private String trainingId;
    private String trainingTitle;
    private String trainingDate;
    private String venue;
    private String trainer;
    private int maximumParticipants;

    public TrainingProgramme(String trainingId, String trainingTitle, String trainingDate, String venue, String trainer, int maximumParticipants) {
        this.trainingId = trainingId;
        this.trainingTitle = trainingTitle;
        this.trainingDate = trainingDate;
        this.venue = venue;
        this.trainer = trainer;
        this.maximumParticipants = maximumParticipants;
    }

    public String getTrainingId() {
        return trainingId;
    }

    public String getTrainingTitle() {
        return trainingTitle;
    }

    public int getMaximumParticipants() {
        return maximumParticipants;
    }
}


class Nomination {
    private Officer officer;
    private TrainingProgramme programme;
    private String nominatedDepartment;

    public Nomination(Officer officer, TrainingProgramme programme, String nominatedDepartment) {
        this.officer = officer;
        this.programme = programme;
        this.nominatedDepartment = nominatedDepartment;
    }

    public Officer getOfficer() {
        return officer;
    }

    public TrainingProgramme getProgramme() {
        return programme;
    }

    public String getNominatedDepartment() {
        return nominatedDepartment;
    }
}


class NominationManager {

    private List<Nomination> nominations = new ArrayList<>();

    public boolean addNomination(Officer officer, TrainingProgramme programme, String department) {

        for (Nomination nomination : nominations) {

            boolean sameOfficer = nomination.getOfficer().getOfficerId().equals(officer.getOfficerId());

            boolean sameProgramme = nomination.getProgramme().getTrainingId().equals(programme.getTrainingId());

            if (sameOfficer && sameProgramme) {

                System.out.println("\nNomination already exists");
                System.out.println("Officer: " + officer.getName());
                System.out.println("Training: " + programme.getTrainingTitle());

                System.out.println("Already nominated by: " + nomination.getNominatedDepartment());

                System.out.println("New nomination from: " + department);

                System.out.println("Registration rejected.");

                return false;
            }
        }

        int currentParticipants = 0;

        for (Nomination nomination : nominations) {

            if (nomination.getProgramme().getTrainingId().equals(programme.getTrainingId())) {

                currentParticipants++;
            }
        }

        if (currentParticipants >= programme.getMaximumParticipants()) {

            System.out.println("\nTraining programme is full.");
            return false;
        }

        Nomination newNomination = new Nomination(officer, programme, department);

        nominations.add(newNomination);

        System.out.println("\nNomination successfully registered.");
        System.out.println("Officer: " + officer.getName());
        System.out.println("Department: " + department);
        System.out.println("Training: " + programme.getTrainingTitle());

        return true;
    }

    public void displayNominations(TrainingProgramme programme) {

        System.out.println("\nNominations");

        for (Nomination nomination : nominations) {

            if (nomination.getProgramme().getTrainingId().equals(programme.getTrainingId())) {

                System.out.println("Officer: " + nomination.getOfficer().getName() + " | Department: " + nomination.getNominatedDepartment());
            }
        }
    }
}


// Main class
public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        TrainingProgramme programme = new TrainingProgramme("TR001", "Leadership Development Programme", "2026-10-15", "Training Venue 01", "External Resource Person", 100);

        Officer officer1 = new Officer("OFF001", "A. Perera", "Finance Division");

        NominationManager manager = new NominationManager();

        System.out.println("NOMINATION SYSTEM");

        manager.addNomination(officer1, programme, "Finance Division");

        manager.addNomination(officer1, programme, "Administration Division");

        manager.displayNominations(programme);

        input.close();
    }
}