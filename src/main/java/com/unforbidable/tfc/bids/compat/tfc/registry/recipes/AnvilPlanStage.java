package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Crafting.PlanRecipe;
import com.dunk.tfc.api.Enums.RuleEnum;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class AnvilPlanStage extends RegistryStage<AnvilPlan> {

    public static final AnvilPlanStage instance = new AnvilPlanStage();

    @Override
    public void add(AnvilPlan plan) {
        Bids.LOG.debug("Register TFC anvil plan '{}'", plan.name);

        try {
            RuleEnum[] rules = new RuleEnum[plan.rules.length];
            for (int i = 0; i < plan.rules.length; i++) {
                rules[i] = RuleEnum.valueOf(plan.rules[i]);
            }
            PlanRecipe planTfc = new PlanRecipe(rules);
            AnvilManager.getInstance().addPlan(plan.name, planTfc);
        } catch (Exception ex) {
            Bids.LOG.error("Failed to register TFC anvil plan '{}'", plan.name, ex);
        }
    }

}
