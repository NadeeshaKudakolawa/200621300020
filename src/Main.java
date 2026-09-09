import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


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
    private int maximumParticipants;

    public TrainingProgramme(String trainingId, String trainingTitle, int maximumParticipants) {
        this.trainingId = trainingId;
        this.trainingTitle = trainingTitle;
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

    private List<Nomination> confirmedNominations = new ArrayList<>();

    private Queue<Nomination> waitingList = new LinkedList<>();

    public boolean addNomination(Officer officer, TrainingProgramme programme, String department) {

        for (Nomination nomination : confirmedNominations) {

            boolean sameOfficer = nomination.getOfficer().getOfficerId().equals(officer.getOfficerId());

            boolean sameProgramme = nomination.getProgramme().getTrainingId().equals(programme.getTrainingId());

            if (sameOfficer && sameProgramme) {
                System.out.println("\nDuplicate nomination detected!");
                System.out.println("Officer: " + officer.getName());
                System.out.println("Training: " + programme.getTrainingTitle());
                System.out.println("Registration rejected.");

                return false;
            }
        }

        for (Nomination nomination : waitingList) {

            boolean sameOfficer = nomination.getOfficer().getOfficerId().equals(officer.getOfficerId());

            boolean sameProgramme = nomination.getProgramme().getTrainingId().equals(programme.getTrainingId());

            if (sameOfficer && sameProgramme) {
                System.out.println("\nDuplicate nomination detected!");
                System.out.println("Officer: " + officer.getName());
                System.out.println("Training: " + programme.getTrainingTitle());
                System.out.println("Registration rejected.");

                return false;
            }
        }

        Nomination newNomination = new Nomination(officer, programme, department);

        if (confirmedNominations.size() < programme.getMaximumParticipants()) {
            confirmedNominations.add(newNomination);

            System.out.println("\nNomination confirmed.");
            System.out.println("Officer: " + officer.getName());
            System.out.println("Training: " + programme.getTrainingTitle());

            return true;
        }

        waitingList.add(newNomination);

        System.out.println("\nTraining programme is full.");
        System.out.println("Officer: " + officer.getName());
        System.out.println("Added to waiting list.");

        return true;
    }

    public void cancelNomination(Officer officer, TrainingProgramme programme) {

        for (int i = 0; i < confirmedNominations.size(); i++) {
            Nomination nomination = confirmedNominations.get(i);

            boolean sameOfficer = nomination.getOfficer().getOfficerId().equals(officer.getOfficerId());

            boolean sameProgramme = nomination.getProgramme().getTrainingId().equals(programme.getTrainingId());

            if (sameOfficer && sameProgramme) {
                confirmedNominations.remove(i);

                System.out.println("\nNomination cancelled.");
                System.out.println("Officer: " + officer.getName());

                promoteFromWaitingList(programme);

                return;
            }
        }

        System.out.println("\nConfirmed nomination not found.");
    }

    private void promoteFromWaitingList(TrainingProgramme programme) {

        for (Nomination nomination : waitingList) {
            boolean sameProgramme = nomination.getProgramme().getTrainingId().equals(programme.getTrainingId());

            if (sameProgramme) {
                waitingList.remove(nomination);
                confirmedNominations.add(nomination);

                System.out.println("\nWaiting list participant promoted.");
                System.out.println("Officer: " + nomination.getOfficer().getName());

                return;
            }
        }

        System.out.println("No participant available " + "on the waiting list.");
    }

    public void displayConfirmed(
            TrainingProgramme programme) {

        System.out.println("\nCONFIRMED PARTICIPANTS");

        for (Nomination nomination : confirmedNominations) {
            if (nomination.getProgramme().getTrainingId().equals(programme.getTrainingId())) {
                System.out.println(nomination.getOfficer().getName() + " - " + nomination.getNominatedDepartment());
            }
        }
    }

    public void displayWaitingList(TrainingProgramme programme) {

        System.out.println("\nWAITING LIST");

        for (Nomination nomination : waitingList) {
            if (nomination.getProgramme().getTrainingId().equals(programme.getTrainingId())) {
                System.out.println(nomination.getOfficer().getName() + " - " + nomination.getNominatedDepartment());
            }
        }
    }
}


public class Main {

    public static void main(String[] args) {

        TrainingProgramme programme = new TrainingProgramme("TR001", "Cybersecurity Awareness Programme", 3);


        Officer officer1 = new Officer("OFF001", "A. Perera", "Finance Division");

        Officer officer2 = new Officer("OFF002", "B. Silva", "Administration Division");

        Officer officer3 = new Officer("OFF003", "C. Fernando", "Finance Division");

        Officer officer4 = new Officer("OFF004", "D. Perera", "Administration Division");

        Officer officer5 = new Officer("OFF005", "E. Silva", "Finance Division");


        NominationManager manager = new NominationManager();

        manager.addNomination(officer1, programme, "Finance Division");

        manager.addNomination(officer2, programme, "Administration Division");

        manager.addNomination(officer3, programme, "Finance Division");


        manager.addNomination(officer4, programme, "Administration Division");

        manager.addNomination(officer5, programme, "Finance Division");

        manager.displayConfirmed(programme);
        manager.displayWaitingList(programme);

        System.out.println("\nCANCELLATION");

        manager.cancelNomination(officer2, programme);

        manager.displayConfirmed(programme);
        manager.displayWaitingList(programme);
    }
}