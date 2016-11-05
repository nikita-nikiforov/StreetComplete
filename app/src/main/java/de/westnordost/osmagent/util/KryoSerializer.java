package de.westnordost.osmagent.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Singleton;

import de.westnordost.osmagent.data.osm.changes.StringMapChanges;
import de.westnordost.osmagent.data.osm.changes.StringMapEntryAdd;
import de.westnordost.osmagent.data.osm.changes.StringMapEntryDelete;
import de.westnordost.osmagent.data.osm.changes.StringMapEntryModify;
import de.westnordost.osmagent.dialogs.opening_hours.CircularSection;
import de.westnordost.osmagent.dialogs.opening_hours.OpeningHoursPerMonth;
import de.westnordost.osmagent.dialogs.opening_hours.OpeningHoursPerWeek;
import de.westnordost.osmapi.map.data.Element;
import de.westnordost.osmapi.map.data.OsmLatLon;
import de.westnordost.osmapi.map.data.OsmRelationMember;
import de.westnordost.osmapi.notes.NoteComment;
import de.westnordost.osmapi.user.User;

@Singleton
public class KryoSerializer implements Serializer
{
	private static final Class[] registeredClasses =
	{
			HashMap.class,
			ArrayList.class,
			OsmLatLon.class,
			Element.Type.class,
			OsmRelationMember.class,
			StringMapChanges.class,
			StringMapEntryAdd.class,
			StringMapEntryDelete.class,
			StringMapEntryModify.class,
			NoteComment.class,
			NoteComment.Action.class,
			Date.class,
			User.class,
			CircularSection.class
	};


	private static final ThreadLocal<Kryo> kryo = new ThreadLocal<Kryo>()
	{
		@Override protected Kryo initialValue()
		{
			Kryo kryo = new Kryo();

			/* Kryo docs say that classes that are registered are serialized more space efficiently
	 		  (so it is not necessary that all classes that are serialized are registered here, but
	 		   it is better) */
			kryo.setRegistrationRequired(true);
			kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
			for(Class reg : registeredClasses)
			{
				kryo.register(reg);
			}
			return kryo;
		}
	};

	@Override public byte[] toBytes(Object object)
	{
		Output output = new Output(1024,-1);
		kryo.get().writeObject(output, object);
		output.close();
		return output.toBytes();
	}

	@Override public <T> T toObject(byte[] bytes, Class<T> type)
	{
		Input input = new Input(bytes);
		T result = kryo.get().readObject(input, type);
		input.close();
		return result;
	}
}