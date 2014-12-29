package jfxtras.internal.scene.control.skin.agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Capture the logic to extract the different types of appointments in one place.
 * 
 * @author Tom Eugelink
 */
public class AllAppointments {

	/**
	 * 
	 */
	public AllAppointments(ObservableList<Agenda.Appointment> appointments) {
		this.appointments = appointments;
		
		appointments.addListener((javafx.collections.ListChangeListener.Change<? extends Appointment> change) -> {
			fireOnChangeListener();
		});

	}
	final ObservableList<Agenda.Appointment> appointments;
	
	/**
	 * fires when something changes in the appointments 
	 */
	public void addOnChangeListener(Runnable runnable) {
		this.runnables.add(runnable);
	}
	public void removeOnChangeListener(Runnable runnable) {
		this.runnables.remove(runnable);
	}
	private List<Runnable> runnables = new ArrayList<>();

	private void fireOnChangeListener() {
		for (Runnable runnable : runnables) {
			runnable.run();
		}
	}
	
	/**
	 * 
	 */
	public List<Appointment> collectWholedayFor(LocalDate localDate) {
		List<Appointment> collectedAppointments = new ArrayList<>();
		
		// scan all appointments and filter the ones for this day
		for (Agenda.Appointment lAppointment : appointments) {
			if (lAppointment.isWholeDay()) {
				
				LocalDate startLocalDate = lAppointment.getDisplayedAtStartLocalDateTime().toLocalDate();
				LocalDate endLocalDate = (lAppointment.getDisplayedAtEndLocalDateTime() == null ? startLocalDate : lAppointment.getDisplayedAtEndLocalDateTime().minusNanos(1).toLocalDate());  // end is exclusive, so subtract one nano
				if ( (startLocalDate.isEqual(localDate) || startLocalDate.isBefore(localDate))
				  && (endLocalDate.isEqual(localDate) || endLocalDate.isAfter(localDate)) 
				) {
					collectedAppointments.add(lAppointment);
				}
			}
		}
		return collectedAppointments;
	}
	
	/**
	 * 
	 */
	public List<Appointment> collectTaskFor(LocalDate localDate) {
		List<Appointment> collectedAppointments = new ArrayList<>();
		
		// scan all appointments and filter the ones for this day
		for (Agenda.Appointment lAppointment : appointments) {
			// an not-wholeday appointment WITHOUT an end is a task
			if (!lAppointment.isWholeDay() && lAppointment.getDisplayedAtEndLocalDateTime() == null) {
				
				if (lAppointment.getDisplayedAtStartLocalDateTime().toLocalDate().isEqual(localDate)) {
					collectedAppointments.add(lAppointment);
				}
			}
		}
		return collectedAppointments;
	}
	
	/**
	 * 
	 */
	public List<Appointment> collectRegularFor(LocalDate localDate) {
		List<Appointment> collectedAppointments = new ArrayList<>();
		
		// scan all appointments and filter the ones for this day
		for (Agenda.Appointment lAppointment : appointments) {
			// an not-wholeday appointment WITH a set enddate is a regular appointment
			if (!lAppointment.isWholeDay() && lAppointment.getDisplayedAtEndLocalDateTime() != null) {
				
				LocalDate startLocalDate = lAppointment.getDisplayedAtStartLocalDateTime().toLocalDate();
				LocalDate endLocalDate = lAppointment.getDisplayedAtEndLocalDateTime().minusNanos(1).toLocalDate();  // end is exclusive, so subtract one nano
				if ( (startLocalDate.isEqual(localDate) || startLocalDate.isBefore(localDate))
				  && (endLocalDate.isEqual(localDate) || endLocalDate.isAfter(localDate)) 
				) {
					collectedAppointments.add(lAppointment);
				}
			}
		}
		return collectedAppointments;
	}
}
