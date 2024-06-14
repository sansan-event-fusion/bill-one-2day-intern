export const getEnv = (varName: string, defaultValue: string): string => {
    const value = process.env[varName];
    if (value === undefined) {
        console.warn(`Environment variable ${varName} is not set. Using default value: ${defaultValue}`);
        return defaultValue;
    }
    return value;
};
