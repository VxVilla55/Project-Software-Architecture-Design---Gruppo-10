/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group10.service.command;

import com.group10.model.MusicCatalogue;
import com.group10.model.TrackComponent;
import com.group10.model.TrackComponent.Tag;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author group10
 * PATTERN: Command. ConcreteCommand, aggiunge uno o piu' tag a una traccia (annullabile).
 */
public class AddTagCommand implements Command {
    private final TrackComponent track;
    private final ArrayList<Tag> tags;

    public AddTagCommand(TrackComponent track, ArrayList<Tag> tags) {
        this.track = track;
        this.tags = tags;
    }
    
    public AddTagCommand(TrackComponent track, Tag tag) {
        this.track = track;
        this.tags = new ArrayList<>();
        tags.add(tag);
    }

    @Override
    public void execute() {
        //aggiunta del tag alla traccia
        track.addTags(tags);
    }

    @Override
    public void undo() {
        //rimozione del tag alla traccia
        track.removeTags(tags);
    }
}
