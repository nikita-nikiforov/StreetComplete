package de.westnordost.streetcomplete.quests.bike_parking_capacity;

import android.os.Bundle;

import javax.inject.Inject;

import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType;
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder;
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataDao;
import de.westnordost.streetcomplete.quests.AbstractQuestAnswerFragment;

public class AddBikeParkingCapacity extends SimpleOverpassQuestType
{
	@Inject
	public AddBikeParkingCapacity(OverpassMapDataDao overpassServer)
	{
		super(overpassServer);
	}

	@Override
	protected String getTagFilters()
	{
		return "nodes, ways with amenity=bicycle_parking and !capacity";
	}

	public AbstractQuestAnswerFragment createForm()
	{
		return new AddBikeParkingCapacityForm();
	}

	public void applyAnswerTo(Bundle answer, StringMapChangesBuilder changes)
	{
		changes.add("capacity", ""+answer.getInt(AddBikeParkingCapacityForm.BIKE_PARKING_CAPACITY));
	}

	@Override public String getCommitMessage()
	{
		return "Add bicycle parking capacities";
	}

	@Override public String getIconName() {	return "bicycle_parking"; }
}
