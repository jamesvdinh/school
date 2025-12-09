import pandas as pd

# Diabetes ICD Codes
DIABETES_CODES = ['250', 'E10', 'E11', 'E13']


def load_data(file_path, usecols=None):
    """Load diagnoses data from a CSV file."""
    return pd.read_csv(
        file_path,
        compression='gzip',
        low_memory=False,
        dtype=str,
        usecols=usecols
    )


def get_diabetic_patients(diagnoses_df):
    """Extract diagnoses related to diabetes."""
    diabetes_diagnoses = diagnoses_df[diagnoses_df['icd_code'].str.startswith(
        tuple(DIABETES_CODES))]
    return diabetes_diagnoses


def filter_to_cohort(df, subject_ids):
    return df[df['subject_id'].isin(subject_ids)]
