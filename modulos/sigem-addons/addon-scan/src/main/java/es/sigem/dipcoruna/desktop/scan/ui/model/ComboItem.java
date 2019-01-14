package es.sigem.dipcoruna.desktop.scan.ui.model;

/**
 * @author evazquezma
 *
 * @param <E>
 */
public class ComboItem<E> {
	public final String label;
	public final E value;

	public ComboItem(final String label, final E value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public E getValue() {
		return value;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ComboItem other = (ComboItem) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
}
