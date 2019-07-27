CREATE TABLE public."user"
(
  name character varying(50) NOT NULL,
  "userId" serial NOT NULL primary key,
  password character varying(50) NOT NULL,
  balance integer NOT NULL,
  "phoneNo" character varying(10) NOT NULL unique,
  email character varying(50) unique
);

CREATE TABLE public.parking
(
  parkingId integer NOT NULL DEFAULT nextval('parking_parkingId_seq'::regclass),
  parkingName character varying(50) NOT NULL,
  address character varying(100) NOT NULL,
  latitude double precision NOT NULL,
  longitude double precision NOT NULL,
  totalSlots integer NOT NULL,
  availableSlots integer NOT NULL,
  CONSTRAINT parking_pkey PRIMARY KEY (parkingId)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.parking
  OWNER TO postgres;

CREATE TABLE public.bookings
(
  bookingId integer NOT NULL DEFAULT nextval('bookings_bookingId_seq'::regclass),
  inTime long NOT NULL,
  outTime long,
  inOtp integer NOT NULL,
  outOtp integer,
  slotDuration integer NOT NULL,
  status varchar(15) NOT NULL,
  bill double precision,
  userId integer NOT NULL,
  parkingId integer NOT NULL,
  CONSTRAINT bookings_pkey PRIMARY KEY (bookingId),
  CONSTRAINT bookings_parkingId_fkey FOREIGN KEY (parkingId)
      REFERENCES public.parking (parkingId) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bookings_userId_fkey FOREIGN KEY (userId)
      REFERENCES public."user" (userId) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.bookings
  OWNER TO postgres;

CREATE TABLE public.history
(
  bookingId integer NOT NULL,
  bill double precision NOT NULL,
  slotDuration integer NOT NULL,
  outTime timestamp NOT NULL,
  inTime timestamp NOT NULL,
  userId integer NOT NULL,
  parkingId integer NOT NULL,
  status varchar(15) NOT NULL,
  CONSTRAINT history_pkey PRIMARY KEY (bookingId),
  CONSTRAINT history_parkingId_fkey FOREIGN KEY (parkingId)
      REFERENCES public.parking (parkingId) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT history_userId_fkey FOREIGN KEY (userId)
      REFERENCES public."user" (userId) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.history
  OWNER TO postgres;


CREATE TABLE public.user
(
  name character varying(50) NOT NULL,
  user_id serial NOT NULL primary key,
  password character varying(50) NOT NULL,
  balance integer NOT NULL,
  phone_no character varying(10) NOT NULL unique,
  email character varying(50) unique
);

CREATE TABLE public.parking
(
  parking_id serial NOT NULL primary key,
  parking_name character varying(50) NOT NULL,
  address character varying(100) NOT NULL,
  latitude double precision NOT NULL,
  longitude double precision NOT NULL,
  total_slots integer NOT NULL,
  available_slots integer NOT NULL
);

CREATE TABLE public.booking
(
  booking_id integer NOT NULL primary key,
  in_time long NOT NULL,
  out_time long,
  in_otp integer NOT NULL,
  out_otp integer,
  slot_duration integer NOT NULL,
  status varchar(15) NOT NULL,
  bill double precision,
  user_id integer NOT NULL REFERENCES "user"(user_id),
  parking_id integer NOT NULL REFERENCES "parking"(parking_id)
);

CREATE TABLE public.history
(
  booking_id integer NOT NULL primary key,
  in_time timestamp NOT NULL,
  out_time timestamp,
  slot_duration integer NOT NULL,
  status varchar(15) NOT NULL,
  bill double precision,
  user_id integer NOT NULL REFERENCES "user"(user_id),
  parking_id integer NOT NULL REFERENCES "parking"(parking_id)
);
