# Landside BC
CREATE
    DATABASE landside;
GRANT ALL
    ON landside.* TO 'user'@'%';
GRANT SHOW
    DATABASES ON *.* TO 'user'@'%';
FLUSH
    PRIVILEGES;

# Warehousing BC
CREATE
    DATABASE warehousing;
GRANT ALL
    ON warehousing.* TO 'user'@'%';
GRANT SHOW
    DATABASES ON *.* TO 'user'@'%';
FLUSH
    PRIVILEGES;

# Waterside BC
CREATE
    DATABASE waterside;
GRANT ALL
    ON waterside.* TO 'user'@'%';
GRANT SHOW
    DATABASES ON *.* TO 'user'@'%';
FLUSH
    PRIVILEGES;

# Invoicing BC
CREATE
    DATABASE invoicing;
GRANT ALL
    ON invoicing.* TO 'user'@'%';
GRANT SHOW
    DATABASES ON *.* TO 'user'@'%';
FLUSH
    PRIVILEGES;