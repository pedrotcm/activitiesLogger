package br.com.activitieslogger.interfaces;

import android.view.View;

/**
 * Created by PedroTome on 21/08/15.
 */
public interface RecyclerViewOnClickListenerHack {
    //Interfaces para funcionar o click na lista de exercicios e na opcao de deletar
    public void onClickListener(View view, int position);
    public void onDeleteClickListener(View view, int position);
}
