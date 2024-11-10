package com.optimagrowth.organization.events.event;

import java.io.Serializable;

import com.optimagrowth.organization.events.model.OrganizationChangeModel;

public sealed interface SourceEvent extends Serializable {

    record OrganizationCreated(OrganizationChangeModel organizationChangeModel) implements SourceEvent {

    }

}
