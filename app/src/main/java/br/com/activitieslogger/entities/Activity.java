package br.com.activitieslogger.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by PedroTome on 21/08/15.
 */
@Table(name="exercicio")
public class Activity extends Model {

    @Column(name="tempo_exercicio")
    private Integer timeActivity;

    @Column(name = "tipo_exercicio")
    private String typeActivity;

    @Column(name = "data_criacao")
    private Date dateCreated;

    public Activity() {
        super();
    }

    public Integer getTimeActivity() {
        return timeActivity;
    }

    public void setTimeActivity(Integer timeActivity) {
        this.timeActivity = timeActivity;
    }

    public String getTypeActivity() {
        return typeActivity;
    }

    public void setTypeActivity(String typeActivity) {
        this.typeActivity = typeActivity;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
