package br.com.activitieslogger.repositories;

import com.activeandroid.query.Select;

import java.util.List;

import br.com.activitieslogger.entities.Activity;

public class ActivityRepository {

	public static List<Activity> getAllActivities() {
		return new Select().from( Activity.class ).execute();

	}

}