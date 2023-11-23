CREATE KEYSPACE IF NOT EXISTS pragmatic
    WITH replication = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
        };
