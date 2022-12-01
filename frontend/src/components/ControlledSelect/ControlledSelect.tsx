import React, { Suspense } from 'react';
import { FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { Control, FieldValues, Controller } from 'react-hook-form';
import { Await } from 'react-router-dom';

interface ControlledSelectProps<DataT, ControlT extends Control<FieldValues, unknown>> {
  data: DataT[];
  control: ControlT;
  errorMessage?: string;
  loadingMessage: string;
  label: string;
  name: string;
  rules: any;
  defaultValue?: string;
  id: string;
  dataTestId: string;
  menuItemKey: string;
  menuItemValue: string;
  menuItemText: string;
}

const ControlledSelect = <DataT, ControlT extends Control<any, unknown>>({
  data, control, errorMessage, loadingMessage, label, name, rules, defaultValue, id, dataTestId,
  menuItemKey, menuItemValue, menuItemText,
}: ControlledSelectProps<DataT, ControlT>) => (
  <Suspense fallback={ <>😸{loadingMessage}😸</> }>
    <Await resolve={ data } errorElement={ <>😿Error😿</> }>
      {(dataArg) => (
        <FormControl fullWidth>
          <InputLabel>{label}</InputLabel>
          <Controller
            name={ name }
            control={ control }
            rules={ rules }
            defaultValue={ defaultValue || '' }
            render={ ({ field }) => (
              <Select
              // eslint-disable-next-line react/jsx-props-no-spreading
                { ...field }
                labelId={ id }
                id={ id }
                label={ label }
                error={ errorMessage !== undefined }
                data-testid={ dataTestId }
              >
                {dataArg.map((item: any) => (
                  <MenuItem key={ item[menuItemKey] } value={ item[menuItemValue] }>
                    {item[menuItemText]}
                  </MenuItem>
                ))}
              </Select>
            ) }
          />
        </FormControl>
      )}
    </Await>
  </Suspense>
  );

export default ControlledSelect;
