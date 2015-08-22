package br.com.activitieslogger.repositories;

import com.activeandroid.query.Select;

import java.util.List;

import br.com.activitieslogger.entities.Activity;

/**
 * Created by PedroTome on 21/08/15.
 */
public class ActivityRepository {

	public static List<Activity> getAllActivities() {
		return new Select().from( Activity.class ).orderBy("data_criacao DESC, tempo_exercicio DESC").execute();

	}

}