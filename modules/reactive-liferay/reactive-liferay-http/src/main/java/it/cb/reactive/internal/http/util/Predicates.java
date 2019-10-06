package it.cb.reactive.internal.http.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class Predicates {

	public static <T> Predicate<T> or(Predicate<T> p1, Predicate<T> p2) {
		Objects.requireNonNull(p1, "p1 is null");
		Objects.requireNonNull(p2, "p2 is null");
		return new PredicateArray<T>(false, p1, p2);
	}

	public static <T> Predicate<T> and(Predicate<T> p1, Predicate<T> p2) {
		Objects.requireNonNull(p1, "p1 is null");
		Objects.requireNonNull(p2, "p2 is null");
		return new PredicateArray<T>(true, p1, p2);
	}

	public static <T> Predicate<T> negative() {
		return ignore -> false;
	}

	public static <T> Predicate<T> positive() {
		return ignore -> true;
	}

	public static boolean isPredicateArray(Predicate predicate) {
		Objects.requireNonNull(predicate, "predicate is null");
		return predicate instanceof PredicateArray;
	}

	public static <T> Predicate<T>[] getPredicateArray(
		Predicate<T> predicate) {

		return isPredicateArray(predicate)
			? ((PredicateArray<T>)predicate)._predicates.clone()
			: _toArray(predicate);
	}

	private static <T> Predicate<T>[] _toArray(Predicate<T> predicate) {
		return (Predicate<T>[])new Predicate[]{predicate};
	}

	private static class PredicateArray<T> implements Predicate<T> {

		@SafeVarargs
		PredicateArray(
			boolean andOperator,
			Predicate<T>...predicates) {
			_andOperator = andOperator;
			_predicates = predicates;;
		}

		@Override
		public boolean test(T t) {
			if (_predicate == null) {
				_predicate = _andOperator ? and() : or();
			}
			return _predicate.test(t);
		}

		private Predicate<T> _or(Predicate<T> p1, Predicate<T> p2) {
			return t -> p1.test(t) || p2.test(t);
		}

		private Predicate<T> _and(Predicate<T> p1, Predicate<T> p2) {
			return t -> p1.test(t) && p2.test(t);
		}

		private Predicate<T> or() {
			return _aggregator(this::_or);
		}

		private Predicate<T> and() {
			return _aggregator(this::_and);
		}

		private Predicate<T> _aggregator(
			BinaryOperator<Predicate<T>> binaryOperator) {
			if (_predicates.length == 0) {
				return s -> false;
			}
			else if (_predicates.length == 1) {
				return _predicates[0];
			}
			else if (_predicates.length == 2) {
				return binaryOperator.apply(_predicates[0], _predicates[1]);
			}
			else if (_predicates.length == 3) {
				return binaryOperator.apply(
					_predicates[0],
					binaryOperator.apply(_predicates[1], _predicates[2]));
			}
			else if (_predicates.length == 4) {
				return binaryOperator.apply(
					binaryOperator.apply(_predicates[0], _predicates[1]),
					binaryOperator.apply(_predicates[2], _predicates[3])
				);
			}
			else {
				return Arrays
					.stream(_predicates)
					.reduce(binaryOperator)
					.orElseGet(Predicates::negative);
			}
		}

		private final Predicate<T>[] _predicates;

		private final boolean _andOperator;

		private volatile Predicate<T> _predicate;

	}

}
