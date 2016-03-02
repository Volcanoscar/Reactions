package com.chrynan.reactions;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chRyNaN on 2/26/2016.
 */
public class ReactionPopup extends PopupWindow {
    private boolean showing;
    private boolean reactionSelected;
    private Reaction reaction;
    private Context context;

    private List<ReactionSelectedListener> listeners;
    private List<VisibilityChangedListener> visibilityListeners;

    private ReactionView view;

    public enum Reaction{
        LIKE, LOVE, LAUGH, WOW, SAD, ANGRY
    }

    public ReactionPopup(Context context){
        super(context);
        init(context);
        this.context = context;
    }

    private void init(Context context){
        showing = false;
        reactionSelected = false;
        reaction = null;
        listeners = new ArrayList<>();
        visibilityListeners = new ArrayList<>();
        view = new ReactionView(context);
        //TODO
    }

    protected void onLike(){

    }

    protected void onLove(){

    }

    protected void onLaugh(){

    }

    protected void onWow(){

    }

    protected void onSad(){

    }

    protected void onAngry(){

    }

    protected void onShow(){

    }

    protected void onHide(){

    }

    public boolean isShowing() {
        return showing;
    }

    public void show(MotionEvent event){
        //TODO
        onShow();
        alertOnShow();
    }

    public void dismiss(){
        //TODO
        onHide();
        alertOnHide();
    }

    public void selectReaction(Reaction reaction){
        handleReactionSelected(reaction);
    }

    public Reaction getSelectedReaction(){
        return reaction;
    }

    public boolean isReactionSelected(){
        return reactionSelected;
    }

    private void handleReactionSelected(Reaction reaction){
        this.reaction = reaction;
        reactionSelected = true;
        if(showing){
            dismiss();
            showing = false;
        }
        switch(reaction){
            case LIKE:
                onLike();
                alertOnLikeSelected();
                break;
            case LOVE:
                onLove();
                alertOnLoveSelected();
                break;
            case LAUGH:
                onLaugh();
                alertOnLaughSelected();
                break;
            case WOW:
                onWow();
                alertOnWowSelected();
                break;
            case SAD:
                onSad();
                alertOnSadSelected();
                break;
            case ANGRY:
                onAngry();
                alertOnAngrySelected();
                break;
        }
    }

    public Context getContext(){
        return context;
    }


    public interface ReactionSelectedListener{
        void onLike();
        void onLove();
        void onLaugh();
        void onWow();
        void onSad();
        void onAngry();
    }

    public void addReactionSelectedListener(ReactionSelectedListener l){
        if(listeners == null){
            listeners = new ArrayList<>();
        }
        listeners.add(l);
    }

    public boolean removeReactionSelectedListener(ReactionSelectedListener l){
        if(listeners != null){
            return listeners.remove(l);
        }
        return false;
    }

    private void alertOnLikeSelected(){
        for(ReactionSelectedListener l : listeners){
            l.onLike();
        }
    }

    private void alertOnLoveSelected(){
        for(ReactionSelectedListener l : listeners){
            l.onLove();
        }
    }

    private void alertOnLaughSelected(){
        for(ReactionSelectedListener l : listeners){
            l.onLaugh();
        }
    }

    private void alertOnWowSelected(){
        for(ReactionSelectedListener l : listeners){
            l.onWow();
        }
    }

    private void alertOnSadSelected(){
        for(ReactionSelectedListener l : listeners){
            l.onSad();
        }
    }

    private void alertOnAngrySelected(){
        for(ReactionSelectedListener l : listeners){
            l.onAngry();
        }
    }


    public interface VisibilityChangedListener{
        void onShow();
        void onHide();
    }

    public void addVisibilityChangedListener(VisibilityChangedListener l){
        if(visibilityListeners == null){
            visibilityListeners = new ArrayList<>();
        }
        visibilityListeners.add(l);
    }

    public boolean removeVisibilityChangedListener(VisibilityChangedListener l){
        if(visibilityListeners != null){
            return visibilityListeners.remove(l);
        }
        return false;
    }

    private void alertOnShow(){
        for(VisibilityChangedListener l : visibilityListeners){
            l.onShow();
        }
    }

    private void alertOnHide(){
        for(VisibilityChangedListener l : visibilityListeners){
            l.onHide();
        }
    }


}
