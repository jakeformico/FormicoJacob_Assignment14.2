package com.optimagrowth.license.events.event;

import java.io.Serializable;

import com.optimagrowth.license.events.model.OrganizationChangeModel;

public sealed interface SourceEvent extends Serializable {

    record OrganizationCreated(OrganizationChangeModel organizationChangeModel) implements SourceEvent {

    }

}
